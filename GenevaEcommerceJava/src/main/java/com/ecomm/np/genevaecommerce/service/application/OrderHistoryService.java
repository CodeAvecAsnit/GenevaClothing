package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.HistoryDTO;
import com.ecomm.np.genevaecommerce.model.dto.OrderDTO;
import com.ecomm.np.genevaecommerce.extra.DateFormat;
import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.UserServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class OrderHistoryService {

    private final UserService userService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderHistoryService(UserServiceImpl userServiceImpl, OrderItemServiceImpl orderItemService) {
        this.userService = userServiceImpl;
        this.orderItemService = orderItemService;
    }

    public Page<HistoryDTO> findUserHistory(int userId, Pageable pageable) {
        OrderDetails orderDetails = userService.findUserById(userId).getUserOrders();
        Page<OrderedItems> orderedItemsPage = orderItemService.findByOrderDetails(orderDetails, pageable);
        return transform(orderedItemsPage);
    }

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


    public Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable){
        Page<OrderedItems> orderedItemsPage = orderItemService.findAllOrdered(pageable);
        return transform(orderedItemsPage);
    }

    public Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable,boolean isActive){
        Page<OrderedItems> orderedItemsPage = orderItemService.findActiveOrderedItems(pageable,isActive);
        return transform(orderedItemsPage);
    }

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
