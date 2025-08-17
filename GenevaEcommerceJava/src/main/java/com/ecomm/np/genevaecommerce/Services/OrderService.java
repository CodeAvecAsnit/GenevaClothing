package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.AddressDTO;
import com.ecomm.np.genevaecommerce.DTO.ItemQuantity;
import com.ecomm.np.genevaecommerce.DTO.OrderDTO;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.OrderDetailsRepository;
import com.ecomm.np.genevaecommerce.Repositories.OrderItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {


    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final UserRepository userRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ItemsRepository itemsRepository;

    @Autowired
    public OrderService(UserRepository userRepository, OrderItemsRepository orderItemsRepository, OrderDetailsRepository orderDetailsRepository, ItemsRepository itemsRepository) {
        this.userRepository = userRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.itemsRepository = itemsRepository;
    }

    public OrderedItems orderItem(OrderDTO orderDTO) {
        Optional<UserModel> userOpt = userRepository.findById(orderDTO.getUser_id());
        Optional<Items> itemOpt = itemsRepository.findById(orderDTO.getItem_id());

        if (userOpt.isPresent() && itemOpt.isPresent()) {
            UserModel user = userOpt.get();

            OrderDetails orderDetails = user.getUserOrders();
            if (orderDetails == null) {
                orderDetails = new OrderDetails();
                orderDetails.setUser(user);
            }

            orderDetails.setDeliveryLocation(orderDTO.getAddress());
            orderDetailsRepository.save(orderDetails);

            OrderedItems orderedItem = new OrderedItems();
            orderedItem.setProcessed(false);
            orderedItem.setMainActive(true);
            orderedItem.setOrderDetails(orderDetails);

            return orderItemsRepository.save(orderedItem);
        } else {
            logger.error("User or Item not found. User ID: {}, Item ID: {}", orderDTO.getUser_id(), orderDTO.getItem_id());
            return null;
        }
    }






    public void checkOutCart(int user_id) throws Exception {
        Optional<UserModel> user = userRepository.findById(user_id);
        if(user.isPresent()){
            UserModel userModel = user.get();
            if(userModel.getUserOrders()==null){
                throw new Exception("No User Information available");
            }
            if(userModel.getCartList()==null) {
                throw new Exception("Cart is empty. Nothing to checkout.");
            }
            OrderDetails orderDetails = userModel.getUserOrders();
            for(Items items : userModel.getCartList()){
                userModel.getCartList().remove(items);
                OrderedItems item = new OrderedItems();
                item.setOrderDetails(orderDetails);
                item.setMainActive(true);
                item.setProcessed(false);
                orderItemsRepository.save(item);
            }
            userRepository.save(userModel);
        }
    }

    public float LiveCalculator2(List<ItemQuantity> iqList){
        float sum = 0f;
        try {
            for (ItemQuantity iq : iqList) {
                sum += itemsRepository.findTotalPrice(iq.getQuantity(), iq.getItemCode());
            }
            return sum;
        }catch (Exception ex){
            logger.warn(ex.getMessage());
            throw new RuntimeException();
        }
    }

    private Items findItemById(int id){
        return itemsRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Cannot find item with the id :"+id)
        );
    }

    public float LiveCalculator1(List<ItemQuantity> itemQuantities) {
        float sum = 0;
        for (ItemQuantity itemQuantity : itemQuantities) {
            Items items = findItemById(itemQuantity.getItemCode());
            if (items.getStock() > itemQuantity.getQuantity()) {
                sum += items.getPrice() * itemQuantity.getQuantity();
            }
        }
        return sum;
    }



    public AddressDTO getAddress(int id)throws Exception{
        Optional<UserModel> optional = userRepository.findById(id);
        if(optional.isEmpty()) throw new NullPointerException("The User does not exist");
        UserModel user = optional.get();
        try{
            OrderDetails od = user.getUserOrders();
            return AddressDTO.buildFromModel(od);
        }catch (NullPointerException ex){
            throw ex;
        }catch (Exception ex){
            return null;
        }
    }

    public AddressDTO addOrUpdateAddress(int id,AddressDTO addressDTO){
        Optional<UserModel> user = userRepository.findById(id);
        if(user.isEmpty()) return null;
        UserModel userModel = user.get();
        OrderDetails od = userModel.getUserOrders();
        if(od==null){
            od = new OrderDetails();
            od.setUser(userModel);
        }
        od.setCity(addressDTO.getCity());
        od.setDeliveryLocation(addressDTO.getStreetName());
        od.setProvince(addressDTO.getProvince());
        OrderDetails orderDetails = orderDetailsRepository.save(od);
        return AddressDTO.buildFromModel(orderDetails);
    }
}
