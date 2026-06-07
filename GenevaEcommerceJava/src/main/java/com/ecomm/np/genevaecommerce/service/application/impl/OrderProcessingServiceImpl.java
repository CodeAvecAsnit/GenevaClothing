package com.ecomm.np.genevaecommerce.service.application.impl;

import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.model.events.OrderCreatedEvent;
import com.ecomm.np.genevaecommerce.model.events.OrderPackedEvent;
import com.ecomm.np.genevaecommerce.service.application.OrderProcessingService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessingServiceImpl implements OrderProcessingService {

    private final OrderItemService orderItemService;
    private final OrderItemAuditService orderItemAuditService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderProcessingServiceImpl(@Qualifier("orderItemServiceImpl") OrderItemService orderItemService,
                                      @Qualifier("orderItemAuditServiceImpl") OrderItemAuditService orderItemAuditService,
                                      ApplicationEventPublisher eventPublisher) {
        this.orderItemService = orderItemService;
        this.orderItemAuditService = orderItemAuditService;
        this.eventPublisher = eventPublisher;
    }


    @Override
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

    @Override
    public boolean setPackedByAdmin(int orderItemCode){
        OrderItemAudit oa = orderItemAuditService.findAuditById(orderItemCode);
        if(oa.isPacked()) return true;
        oa.setPacked(true);
        OrderItemAudit updated = orderItemAuditService.saveAudit(oa);
        if(checkAllPacked(updated.getOrderedItems())){
            setOrderPacked(updated.getOrderedItems());
        }
        return true;
    }

    @Override
    public boolean setAllPackedAdmin(int orderCode){
        OrderedItems oI = orderItemService.findOrderedItemsById(orderCode);
        if(checkAllPacked(oI)) {
            setOrderPacked(oI);
            return true;
        }
        List<OrderItemAudit> olList = oI.getOrderItemAuditList();
        olList.forEach(oa->oa.setPacked(true));
        orderItemAuditService.saveAllItemOrders(olList);
        setOrderPacked(oI);
        return true;
    }

    @Override
    public boolean setApprovedByAdmin(int orderItemCode) {
        OrderItemAudit oa= orderItemAuditService.findAuditById(orderItemCode);
        if(oa.isApproved()) return true;
        oa.setApproved(true);
        OrderItemAudit updated = orderItemAuditService.saveAudit(oa);
        if(checkAllApproved(updated.getOrderedItems())){
            setOrderApproved(updated.getOrderedItems());
        }
        return true;
    }

    @Override
    public boolean setAllApprovedAdmin(int orderCode){
        OrderedItems oI = orderItemService.findOrderedItemsById(orderCode);
        if(checkAllApproved(oI)) {
            setOrderApproved(oI);
            return true;
        }
        List<OrderItemAudit> olList = oI.getOrderItemAuditList();
        olList.forEach(oa->oa.setApproved(true));
        orderItemAuditService.saveAllItemOrders(olList);
        setOrderApproved(oI);
        return true;
    }

    //checks if all the items of orderItems are packed or not
    private boolean checkAllPacked(OrderedItems orderedItems){
        return orderedItems.getOrderItemAuditList().stream()
                .allMatch(OrderItemAudit::isPacked);
    }

    private boolean checkAllApproved(OrderedItems orderedItems){
        return orderedItems.getOrderItemAuditList().stream()
                .allMatch(OrderItemAudit::isApproved);
    }

    //this method is triggered only once so the packed email is also sent only once
    private void setOrderPacked(OrderedItems orderedItems){
        if(!orderedItems.isProcessed()){
            orderedItems.setProcessed(true);
            OrderedItems saved = orderItemService.saveOrderedItems(orderedItems);
            eventPublisher.publishEvent(new OrderPackedEvent(saved.getUserEmail(),saved));
        }
    }

    private void setOrderApproved(OrderedItems orderedItems){
        if(!orderedItems.isApproved()){
            orderedItems.setApproved(true);
            OrderedItems saved = orderItemService.saveOrderedItems(orderedItems);
            List<OrderItemAudit> items = saved.getOrderItemAuditList();
            eventPublisher.publishEvent(new OrderCreatedEvent(saved.getUserEmail(),saved,items));
        }
    }
}
