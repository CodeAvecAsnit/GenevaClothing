package com.ecomm.np.genevaecommerce.service.application.impl;

import com.ecomm.np.genevaecommerce.model.dto.HistoryDTO;
import com.ecomm.np.genevaecommerce.model.dto.OrderDTO;
import com.ecomm.np.genevaecommerce.extra.util.DateFormat;
import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.service.application.OrderHistoryService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final UserService userService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderHistoryServiceImpl(@Qualifier("userServiceImpl") UserService userService,
                                   @Qualifier("orderItemServiceImpl") OrderItemService orderItemService) {
        this.userService = userService;
        this.orderItemService = orderItemService;
    }

    @Override
    public Page<HistoryDTO> findUserHistory(int userId, Pageable pageable) {
        OrderDetails orderDetails = userService.findUserById(userId).getUserOrders();
        Page<OrderedItems> orderedItemsPage = orderItemService.findByOrderDetails(orderDetails, pageable);
        return transform(orderedItemsPage);
    }

    @Override
    public OrderDTO findOrderData(int orderId, int userId){
        OrderedItems orderedItems = orderItemService.findOrderedItemsById(orderId);
        int UserId = orderedItems.getOrderDetails().getUser().getUserId();
        if(UserId!=userId){
            throw new AccessDeniedException("Access Denied");
        }
        OrderDTO dto = OrderDTO.buildFromOrderItems(orderedItems);
        dto.setOrderDate(DateFormat.buildDate(orderedItems.getOrderInitiatedDate()));
        return dto;
    }

    @Override
    public Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable){
        Page<OrderedItems> orderedItemsPage = orderItemService.findAllOrdered(pageable);
        return transform(orderedItemsPage);
    }

    @Override
    public Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable,boolean isActive){
        Page<OrderedItems> orderedItemsPage = orderItemService.findActiveOrderedItems(pageable,isActive);
        return transform(orderedItemsPage);
    }

    @Override
    public OrderDTO findOrderDataAdmin(int orderId){
        OrderedItems orderedItems = orderItemService.findOrderedItemsById(orderId);
        OrderDTO dto = OrderDTO.buildFromOrderItems(orderedItems);
        dto.setOrderDate(DateFormat.buildDate(orderedItems.getOrderInitiatedDate()));
        return dto;
    }

    private Page<HistoryDTO> transform(Page<OrderedItems> orderedItemsPage){
        return orderedItemsPage.map(ot -> {
            HistoryDTO dto = HistoryDTO.buildFromOrderItems(ot);
            dto.setOrderDate(DateFormat.buildDate(ot.getOrderInitiatedDate()));
            return dto;
        });
    }
}
