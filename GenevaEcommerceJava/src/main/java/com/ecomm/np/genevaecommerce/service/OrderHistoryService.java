package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.dto.HistoryDTO;
import com.ecomm.np.genevaecommerce.dto.OrderDTO;
import com.ecomm.np.genevaecommerce.extra.DateFormat;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.OrderDetails;
import com.ecomm.np.genevaecommerce.model.OrderItemAudit;
import com.ecomm.np.genevaecommerce.model.OrderedItems;
import com.ecomm.np.genevaecommerce.repository.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.IOrderItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryService {

    private final BasicService basicService;
    private final IOrderItemService orderItemService;
    private final OrderItemAuditRepository orderItemAuditRepository;

    @Autowired
    public OrderHistoryService(BasicService basicService, OrderItemService orderItemService, OrderItemAuditRepository orderItemAuditRepository) {
        this.basicService = basicService;
        this.orderItemService = orderItemService;
        this.orderItemAuditRepository = orderItemAuditRepository;
    }

    public Page<HistoryDTO> findUserHistory(int userId, Pageable pageable) {
        OrderDetails orderDetails = basicService.findUserById(userId).getUserOrders();
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


    private Page<HistoryDTO> transform(Page<OrderedItems> orderedItemsPage){
        return orderedItemsPage.map(ot -> {
            HistoryDTO dto = HistoryDTO.buildFromOrderItems(ot);
            dto.setOrderDate(DateFormat.buildDate(ot.getOrderInitiatedDate()));
            return dto;
        });
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

    public boolean setDeliveredAdmin(int orderId){
        OrderedItems orderedItems = orderItemService.findOrderedItemsById(orderId);
        if(!orderedItems.isProcessed()){
            throw new RuntimeException("Cannot set delivered because order is yet to be packed");
        }
        if(!orderedItems.isMainActive()){throw new RuntimeException("Order has already been delivered");}
        orderedItems.setMainActive(false);
        orderItemService.saveOrderedItems(orderedItems);
        return true;
    }

    public boolean setPackedByAdmin(int orderItemCode){
        OrderItemAudit oa = findOrderedItemAudit(orderItemCode);
        if(oa.isPacked()) return true;
        oa.setPacked(true);
        orderItemAuditRepository.save(oa);
        if(checkAllPacked(oa.getOrderedItems())){
            setOrderPacked(oa.getOrderedItems());
        }
        return true;
    }

    public boolean setAllPackedAdmin(int orderCode){
        OrderedItems oI = orderItemService.findOrderedItemsById(orderCode);
        if(checkAllPacked(oI)) {
            setOrderPacked(oI);
            return true;
        }
        List<OrderItemAudit> olList = oI.getOrderItemAuditList();
        olList.forEach(oa->oa.setPacked(true));
        orderItemAuditRepository.saveAll(olList);
        setOrderPacked(oI);
        return true;
    }


    private OrderItemAudit findOrderedItemAudit(int orderItemCode){
        return orderItemAuditRepository.findById(orderItemCode).orElseThrow(
                ()-> new ResourceNotFoundException("The requested ordered item was not found"));

    }

    public boolean checkAllPacked(OrderedItems orderedItems){
        return orderedItems.getOrderItemAuditList().stream()
                .allMatch(OrderItemAudit::isPacked);
    }

    private void setOrderPacked(OrderedItems orderedItems){
        if(!orderedItems.isProcessed()){
            orderedItems.setProcessed(true);
            orderItemService.saveOrderedItems(orderedItems);
        }
    }
}
