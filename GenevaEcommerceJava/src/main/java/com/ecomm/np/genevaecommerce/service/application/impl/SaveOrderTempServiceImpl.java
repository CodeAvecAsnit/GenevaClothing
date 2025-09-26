package com.ecomm.np.genevaecommerce.service.application.impl;

import com.ecomm.np.genevaecommerce.extra.exception.CodeErrorException;
import com.ecomm.np.genevaecommerce.extra.components.ItemMapComp;
import com.ecomm.np.genevaecommerce.extra.exception.OutOfStockException;
import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.CheckDTO;
import com.ecomm.np.genevaecommerce.model.dto.DisplayItemsDTO;
import com.ecomm.np.genevaecommerce.model.dto.QuantityItemDTO;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.application.SaveOrderTempService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SaveOrderTempServiceImpl implements SaveOrderTempService {

    private final ItemMapComp itemMapComp;
    private final SecureRandom secureRandom;
    private final UserService userService;

    private Cache<Integer,List<QuantityItemDTO>> itemQuantityMap;

    public SaveOrderTempServiceImpl(ItemMapComp itemMapComp, SecureRandom secureRandom,
                                    @Qualifier("userServiceImpl") UserService userService) {
        this.itemMapComp = itemMapComp;
        this.secureRandom = secureRandom;
        this.userService = userService;
    }

    @PostConstruct
    public void init(){
        this.itemQuantityMap = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();
    }

    @Override
    public int processAndSaveRequest(List<QuantityItemDTO> itemQuantities)throws OutOfStockException, ResourceNotFoundException {
        Map<Integer, Items> ItemMap =itemMapComp.getItemMap(itemQuantities) ;
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

    @Override
    public int processSingleItem(int itemId,int quantity,String size){
        Items item = itemMapComp.findItemById(itemId);
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

    @Override
    public CheckDTO fetchCheckPage(int code, int userId){
        code-= userId;
        List<QuantityItemDTO> iqList = itemQuantityMap.getIfPresent(code);
        if(iqList.isEmpty()||iqList==null){
            throw new CodeErrorException("The server took too long to respond");
        }
        UserModel userModel = userService.findUserById(userId);
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

    private List<DisplayItemsDTO> findItemDisplayList(List<QuantityItemDTO> itemQuantityList) {
        Map<Integer, Items> itemMap = itemMapComp.getItemMap(itemQuantityList);
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
}
