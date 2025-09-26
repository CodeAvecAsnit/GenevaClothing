package com.ecomm.np.genevaecommerce.service.checkout.impl;

import com.ecomm.np.genevaecommerce.extra.components.ItemMapComp;
import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import com.ecomm.np.genevaecommerce.model.dto.QuantityItemDTO;
import com.ecomm.np.genevaecommerce.model.entity.*;
import com.ecomm.np.genevaecommerce.repository.OrderDetailsRepository;
import com.ecomm.np.genevaecommerce.service.checkout.OrderCreationService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class OrderCreationServiceImpl implements OrderCreationService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemService orderItemService;
    private final ItemMapComp itemMapComp;

    public OrderCreationServiceImpl(OrderDetailsRepository orderDetailsRepository,
                                    @Qualifier("orderItemServiceImpl") OrderItemService orderItemService,
                                    ItemMapComp itemMapComp) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderItemService = orderItemService;
        this.itemMapComp = itemMapComp;
    }

    @Override
    @Transactional
    public OrderedItems createOrder(CheckoutIncDTO checkDTO, UserModel userModel) {
        OrderDetails od = userModel.getUserOrders();
        if (od == null) {
            od = new OrderDetails();
            od.setUser(userModel);
        }
        updateOrderDetails(od, checkDTO);
        OrderedItems orderedItems = createOrderedItems(checkDTO);
        OrderDetails savedDetails = orderDetailsRepository.save(od);
        orderedItems.setOrderDetails(savedDetails);
        return orderItemService.saveOrderedItems(orderedItems);
    }

    private void updateOrderDetails(OrderDetails od, CheckoutIncDTO checkDTO) {
        od.setProvince(checkDTO.getProvince());
        od.setPhoneNumber(checkDTO.getPhoneNumber());
        od.setCity(checkDTO.getCity());
        od.setDeliveryLocation(checkDTO.getDeliveryLocation());
    }

    private OrderedItems createOrderedItems(CheckoutIncDTO checkDTO) {
        OrderedItems orderedItems = new OrderedItems();
        orderedItems.setMainActive(true);
        orderedItems.setProcessed(false);
        orderedItems.setPaidPrice(BigDecimal.ZERO);

        Map<Integer, Items> itemsMap = itemMapComp.getItemMap(checkDTO.getQuantities());
        for (QuantityItemDTO dto : checkDTO.getQuantities()) {
            Items item = itemsMap.get(dto.getItemCode());
            OrderItemAudit audit = createOrderItemAudit(dto, item, orderedItems);
            orderedItems.getOrderItemAuditList().add(audit);
        }
        orderedItems.findTotalOrderPrice();

        return orderedItems;
    }

    private OrderItemAudit createOrderItemAudit(QuantityItemDTO dto, Items item, OrderedItems orderedItems) {
        OrderItemAudit audit = new OrderItemAudit();
        audit.setActive(true);
        audit.setPacked(false);
        audit.setSize(dto.getSize());
        audit.setQuantity(dto.getQuantity());
        audit.setItem(item);
        audit.setTotalPrice();
        audit.setOrderedItems(orderedItems);
        return audit;
    }
}
