package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.extra.ItemMapComp;
import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import com.ecomm.np.genevaecommerce.model.dto.QuantityItemDTO;
import com.ecomm.np.genevaecommerce.model.entity.*;
import com.ecomm.np.genevaecommerce.repository.*;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class CheckoutService {

    private final Logger logger = LoggerFactory.getLogger(CheckoutService.class);
    private final UserService UserService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ItemMapComp itemMapComp;

    @Autowired
    public CheckoutService(UserServiceImpl userServiceImpl, OrderDetailsRepository orderDetailsRepository, ItemMapComp itemMapComp){

        this.UserService = userServiceImpl;
        this.orderDetailsRepository = orderDetailsRepository;
        this.itemMapComp = itemMapComp;
    }

    @Transactional
    public boolean checkoutOrder(CheckoutIncDTO checkDTO, int userId) {
        logger.info(checkDTO.toString());
        try {
            UserModel userModel = UserService.findUserById(userId);
            OrderDetails od = userModel.getUserOrders();
            if (od == null) {
                od = new OrderDetails();
                od.setUser(userModel);
            }
            od.setProvince(checkDTO.getProvince());
            od.setPhoneNumber(checkDTO.getPhoneNumber());
            od.setCity(checkDTO.getCity());
            od.setDeliveryLocation(checkDTO.getDeliveryLocation());

            OrderedItems orderedItems = new OrderedItems();
            orderedItems.setMainActive(true);
            orderedItems.setProcessed(false);
            orderedItems.setPaidPrice(BigDecimal.ZERO);

            orderedItems.setOrderDetails(od);
            od.getOrderedItems().add(orderedItems);

            Map<Integer, Items> itemsMap = itemMapComp.getItemMap(checkDTO.getQuantities());
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
