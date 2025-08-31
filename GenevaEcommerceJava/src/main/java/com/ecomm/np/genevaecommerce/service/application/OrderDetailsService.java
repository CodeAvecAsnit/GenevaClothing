package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.AddressDTO;
import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.repository.OrderDetailsRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.IUserService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final IUserService IUserService;

    @Autowired
    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository, UserService userServiceImpl) {
        this.IUserService = userServiceImpl;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public AddressDTO getAddress(int id){
        UserModel user = IUserService.findUserById(id);
        OrderDetails od = user.getUserOrders();
        return AddressDTO.buildFromModel(od);
    }

    public AddressDTO addOrUpdateAddress(int id,AddressDTO addressDTO){
        UserModel userModel = IUserService.findUserById(id);
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
