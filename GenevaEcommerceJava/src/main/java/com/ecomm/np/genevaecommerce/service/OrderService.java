package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.dto.AddressDTO;
import com.ecomm.np.genevaecommerce.model.OrderDetails;
import com.ecomm.np.genevaecommerce.model.UserModel;
import com.ecomm.np.genevaecommerce.repository.OrderDetailsRepository;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final UserRepository userRepository;

    private final OrderDetailsRepository orderDetailsRepository;


    @Autowired
    public OrderService(UserRepository userRepository, OrderDetailsRepository orderDetailsRepository) {
        this.userRepository = userRepository;
        this.orderDetailsRepository = orderDetailsRepository;
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
