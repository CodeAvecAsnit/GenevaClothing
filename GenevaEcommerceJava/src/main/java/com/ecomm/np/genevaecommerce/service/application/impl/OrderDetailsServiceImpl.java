package com.ecomm.np.genevaecommerce.service.application.impl;

import com.ecomm.np.genevaecommerce.model.dto.AddressDTO;
import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.repository.OrderDetailsRepository;
import com.ecomm.np.genevaecommerce.service.application.OrderDetailsService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final UserService UserService;

    @Autowired
    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository,
                                   @Qualifier("userServiceImpl") UserService userService) {
        this.UserService = userService;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public AddressDTO getAddress(int id){
        UserModel user = UserService.findUserById(id);
        OrderDetails od = user.getUserOrders();
        return AddressDTO.buildFromModel(od);
    }

    @Override
    public AddressDTO addOrUpdateAddress(int id,AddressDTO addressDTO){
        UserModel userModel = UserService.findUserById(id);
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
