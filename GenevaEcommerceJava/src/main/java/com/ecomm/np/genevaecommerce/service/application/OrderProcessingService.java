package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemAuditServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessingService {

    private final OrderItemService orderItemService;
    private final OrderItemAuditService orderItemAuditService;

    public OrderProcessingService(OrderItemServiceImpl orderItemServiceImpl, OrderItemAuditServiceImpl orderItemAuditServiceImpl) {
        this.orderItemService = orderItemServiceImpl;
        this.orderItemAuditService = orderItemAuditServiceImpl;
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
        OrderItemAudit oa = orderItemAuditService.findAuditById(orderItemCode);
        if(oa.isPacked()) return true;
        oa.setPacked(true);
        orderItemAuditService.saveAudit(oa);
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
        orderItemAuditService.saveAllItemOrders(olList);
        setOrderPacked(oI);
        return true;
    }
    private boolean checkAllPacked(OrderedItems orderedItems){
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
