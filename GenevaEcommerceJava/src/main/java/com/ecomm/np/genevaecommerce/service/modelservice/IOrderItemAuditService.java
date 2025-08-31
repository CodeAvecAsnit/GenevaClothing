package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;

import java.util.List;

public interface IOrderItemAuditService {
    List<Integer> findTopSellingItemIds();
    Integer totalItemOrderedCount();
    Integer findTotalItemsPackedCount(boolean val);
    Integer totalOrders(int itemId);
    OrderItemAudit findAuditById(int id);
    void saveAllItemOrders(List<OrderItemAudit> orderItemAudits);
    void saveAudit(OrderItemAudit orderItemAudit);
}
