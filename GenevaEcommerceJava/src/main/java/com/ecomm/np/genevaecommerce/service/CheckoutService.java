package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.dto.CheckDTO;
import com.ecomm.np.genevaecommerce.dto.CheckoutIncDTO;
import com.ecomm.np.genevaecommerce.dto.DisplayItemsDTO;
import com.ecomm.np.genevaecommerce.dto.QuantityItemDTO;
import com.ecomm.np.genevaecommerce.extra.CodeErrorException;
import com.ecomm.np.genevaecommerce.extra.OutOfStockException;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.*;
import com.ecomm.np.genevaecommerce.repository.*;
import com.ecomm.np.genevaecommerce.service.modelservice.IItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.IUserService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final Logger logger = LoggerFactory.getLogger(CheckoutService.class);
    private final IUserService IUserService;
    private final IItemService itemService;
    private final OrderDetailsRepository orderDetailsRepository;

    private Cache<Integer,List<QuantityItemDTO>> itemQuantityMap;
    private final SecureRandom secureRandom;

    @Autowired
    public CheckoutService(SecureRandom secureRandom, UserService userServiceImpl, ItemService itemServiceImpl, OrderDetailsRepository orderDetailsRepository){
        this.itemService = itemServiceImpl;
        this.secureRandom = secureRandom;
        this.IUserService = userServiceImpl;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @PostConstruct
    public void init(){
        this.itemQuantityMap = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();
    }

    public int processAndSaveRequest(List<QuantityItemDTO> itemQuantities)throws OutOfStockException,ResourceNotFoundException{
        Map<Integer, Items> ItemMap = getItemMap(itemQuantities) ;
        if(ItemMap==null||ItemMap.isEmpty())return 0;
        for(QuantityItemDTO itemQuantity : itemQuantities){
            Items item = ItemMap.get(itemQuantity.getItemCode());
            if(item==null){
                throw new ResourceNotFoundException("The Item was not found so cannot process the request");
            }
                if(itemQuantity.getQuantity()==0){
                    itemQuantities.remove(itemQuantity);
                }
                if (item.getStock() < itemQuantity.getQuantity()) {
                    throw new OutOfStockException(item.getItemName()+" out of stock");
                }
        }
        int code = secureRandom.nextInt(100000,1000000);
        itemQuantityMap.put(code,itemQuantities);
        return code;
    }


    private Map<Integer,Items> getItemMap(List<QuantityItemDTO> itemQuantities){
        if(itemQuantities==null||itemQuantities.isEmpty()) return null;
        List<Integer> ids = itemQuantities.stream().map(QuantityItemDTO::getItemCode).toList();
        return itemService.findAllByListOfIds(ids).stream().collect(Collectors.toMap(Items::getItemCode, x->x));
    }


    public float liveCalculator(List<QuantityItemDTO> itemQuantities){
        Map<Integer, Items> ItemMap = getItemMap(itemQuantities);
        if(ItemMap==null) return 0f;
        float sum = 0;
        for(QuantityItemDTO quantityItem :itemQuantities ){
            Items item = ItemMap.get(quantityItem.getItemCode());
            if(item==null){
                logger.warn("Item with id : "+quantityItem.getItemCode()+" not found.");
                continue;
            }
            if(item.getStock()>quantityItem.getQuantity()){
                sum+=item.getPrice()*quantityItem.getQuantity();
            }else{
                logger.warn("Insufficient Stock for item "+item.getItemName());
            }
        }
        return sum;
    }


    public int processSingleItem(int itemId,int quantity,String size){
        Items item = itemService.findItemById(itemId);
        if(item.getStock()<quantity){
            throw new OutOfStockException(item.getItemName()+" is out of stock.");
        }
        QuantityItemDTO iq = new QuantityItemDTO(itemId,size,quantity);
        List<QuantityItemDTO> iqLists = new ArrayList<>();
        iqLists.add(iq);
        int code = secureRandom.nextInt(10000,1000000);
        itemQuantityMap.put(code,iqLists);
        return code;
    }


    private List<DisplayItemsDTO> findItemDisplayList(List<QuantityItemDTO> itemQuantityList) {
        Map<Integer, Items> itemMap = getItemMap(itemQuantityList);
        List<DisplayItemsDTO> displayItems = new ArrayList<>();
        if (itemMap == null || itemMap.isEmpty()) {
            return displayItems;
        }
        for (QuantityItemDTO iq : itemQuantityList) {
            Items item = itemMap.get(iq.getItemCode());
            if (item == null) {
                throw new IllegalArgumentException("Item with code " + iq.getItemCode() + " not found.");
            }
            if (item.getStock() < iq.getQuantity()) {
                throw new OutOfStockException(item.getItemName() + " is out of stock");
            }
            displayItems.add(new DisplayItemsDTO(item, iq.getQuantity(),iq.getSize()));
        }
        return displayItems;
    }

    public CheckDTO fetchCheckPage(int code,int userId){
        code-= userId;
        List<QuantityItemDTO> iqList = itemQuantityMap.getIfPresent(code);
        if(iqList.isEmpty()||iqList==null){
            throw new CodeErrorException("The server took too long to respond");
        }
        UserModel userModel = IUserService.findUserById(userId);
        CheckDTO checkDTO = new CheckDTO();
        OrderDetails orderDetails = userModel.getUserOrders();
        if(orderDetails==null){
            checkDTO.setDeliveryLocation("");
            checkDTO.setProvince("");
            checkDTO.setCity("");
            checkDTO.setPhoneNumber("");
        }else{
            checkDTO.setCity(orderDetails.getCity());
            checkDTO.setDeliveryLocation(orderDetails.getDeliveryLocation());
            checkDTO.setPhoneNumber(orderDetails.getPhoneNumber());
            checkDTO.setProvince(orderDetails.getProvince());
            checkDTO.setPhoneNumber(orderDetails.getPhoneNumber());
        }
        checkDTO.setDisplayItemsDTOList(findItemDisplayList(iqList));
        checkDTO.findTotalPrice();
        return checkDTO;
    }

    public boolean checkoutOrder(CheckoutIncDTO checkDTO, int userId) {
        logger.info(checkDTO.toString());
        try {
            UserModel userModel = IUserService.findUserById(userId);
            OrderDetails od = userModel.getUserOrders();
            if (od == null) {
                od = new OrderDetails();
                od.setUser(userModel);
            }
            od.setProvince(checkDTO.getProvince());
            od.setPhoneNumber(checkDTO.getPhoneNumber());
            od.setCity(checkDTO.getCity());
            od.setDeliveryLocation(checkDTO.getDeliveryLocation());//can check weather the old and new data are same or not and save accordingly here

            OrderedItems orderedItems = new OrderedItems();
            orderedItems.setMainActive(true);
            orderedItems.setProcessed(false);
            orderedItems.setPaidPrice(BigDecimal.ZERO);

            orderedItems.setOrderDetails(od);
            od.getOrderedItems().add(orderedItems);

            Map<Integer, Items> itemsMap = getItemMap(checkDTO.getQuantities());
            for (QuantityItemDTO dto : checkDTO.getQuantities()) {
                Items item = itemsMap.get(dto.getItemCode());
                OrderItemAudit audit = new OrderItemAudit();
                audit.setActive(true);
                audit.setPacked(false);
                audit.setSize(dto.getSize());
                audit.setQuantity(dto.getQuantity());
                audit.setItem(item);
                audit.setTotalPrice();
                audit.setOrderedItems(orderedItems);
                orderedItems.getOrderItemAuditList().add(audit);
            }
            orderedItems.findTotalOrderPrice();
            orderDetailsRepository.save(od);
            return true;
        } catch (Exception ex) {
            logger.error("Checkout failed: {}", ex.getMessage(), ex);
            return false;
        }
    }
}
