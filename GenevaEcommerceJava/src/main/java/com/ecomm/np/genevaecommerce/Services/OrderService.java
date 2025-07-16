package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.OrderDTO;
import com.ecomm.np.genevaecommerce.DTO.UpdateAdressDTO;
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
            Items item = itemOpt.get();

            OrderDetails orderDetails = user.getUserOrders();
            if (orderDetails == null) {
                orderDetails = new OrderDetails();
                orderDetails.setUser(user);
            }

            orderDetails.setDeliveryLocation(orderDTO.getAddress());
            orderDetailsRepository.save(orderDetails);

            OrderedItems orderedItem = new OrderedItems();
            orderedItem.setQuantity(orderDTO.getQuantity());
            orderedItem.setProcessed(false);
            orderedItem.setActive(true);
            orderedItem.setItem(item);
            orderedItem.setOrderDetails(orderDetails);

            return orderItemsRepository.save(orderedItem);
        } else {
            logger.error("User or Item not found. User ID: {}, Item ID: {}", orderDTO.getUser_id(), orderDTO.getItem_id());
            return null;
        }
    }


    public OrderDetails updateAddress(UpdateAdressDTO updateAdressDTO){
        Optional<UserModel> userModel = userRepository.findById(updateAdressDTO.getUserId());
        if(userModel.isPresent()){
            UserModel userApp = userModel.get();
            OrderDetails orderDetails = userApp.getUserOrders();
            if(orderDetails==null){
                orderDetails = new OrderDetails();
                orderDetails.setUser(userApp);
            }
            orderDetails.setDeliveryLocation(updateAdressDTO.getAddress());
            OrderDetails or = orderDetailsRepository.save(orderDetails);
            userApp.setUserOrders(orderDetails);
            userRepository.save(userApp);
            return or;
        }else{
            logger.error("User Not found");
        }
        return null;
    }

}
