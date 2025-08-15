package com.ecomm.np.genevaecommerce.Services;


import com.ecomm.np.genevaecommerce.DTO.CheckDTO;
import com.ecomm.np.genevaecommerce.DTO.DisplayItemsDTO;
import com.ecomm.np.genevaecommerce.DTO.ItemQuantity;
import com.ecomm.np.genevaecommerce.Extras.CodeErrorException;
import com.ecomm.np.genevaecommerce.Extras.OutOfStockException;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final Logger logger = LoggerFactory.getLogger(CheckoutService.class);
    private final UserRepository userRepository;

    private final ItemsRepository itemsRepository;

    private Cache<Integer,List<ItemQuantity>> itemQuantityMap;

    private final SecureRandom secureRandom;

    @Autowired
    public CheckoutService(ItemsRepository itemsRepository, SecureRandom secureRandom, UserRepository userRepository){
        this.itemsRepository = itemsRepository;
        this.secureRandom = secureRandom;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init(){
        this.itemQuantityMap = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    }

    public int processAndSaveRequest(List<ItemQuantity> itemQuantities)throws OutOfStockException,Exception{
        Map<Integer, Items> ItemMap = getItemMap(itemQuantities) ;
        if(ItemMap==null||ItemMap.isEmpty())return 0;
        for(ItemQuantity itemQuantity : itemQuantities){
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


    private Map<Integer,Items> getItemMap(List<ItemQuantity> itemQuantities){
        if(itemQuantities==null||itemQuantities.isEmpty()) return null;
        List<Integer> ids = itemQuantities.stream().map(ItemQuantity::getItemCode).toList();
        return itemsRepository.findAllById(ids).stream().collect(Collectors.toMap(Items::getItemCode,x->x));
    }


    public float liveCalculator(List<ItemQuantity> itemQuantities){
        Map<Integer, Items> ItemMap = getItemMap(itemQuantities);
        if(ItemMap==null) return 0f;
        float sum = 0;
        for(ItemQuantity quantityItem :itemQuantities ){
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

    private UserModel findUserById(int userId){
        return userRepository.findById(userId).orElseThrow(()->
                new UsernameNotFoundException("User was not found."));
    }

    private List<DisplayItemsDTO> findItemDisplayList(List<ItemQuantity> itemQuantityList) {
        Map<Integer, Items> itemMap = getItemMap(itemQuantityList);
        List<DisplayItemsDTO> displayItems = new ArrayList<>();

        if (itemMap == null || itemMap.isEmpty()) {
            return displayItems;
        }
        for (ItemQuantity iq : itemQuantityList) {
            Items item = itemMap.get(iq.getItemCode());
            if (item == null) {
                throw new IllegalArgumentException("Item with code " + iq.getItemCode() + " not found.");
            }
            if (item.getStock() < iq.getQuantity()) {
                throw new OutOfStockException(item.getItemName() + " is out of stock");
            }
            displayItems.add(new DisplayItemsDTO(item, iq.getQuantity()));
        }
        return displayItems;
    }

    public CheckDTO fetchCheckPage(int code,int userId){
        code-= userId;
        List<ItemQuantity> iqList = itemQuantityMap.getIfPresent(code);
        if(iqList.isEmpty()||iqList==null){
            throw new CodeErrorException("The server took too long to respond");
        }
        UserModel userModel = findUserById(userId);
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
}
