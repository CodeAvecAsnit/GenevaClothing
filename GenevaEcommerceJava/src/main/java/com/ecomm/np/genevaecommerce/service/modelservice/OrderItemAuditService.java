package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.repository.OrderItemAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemAuditService implements IOrderItemAuditService{

    private final OrderItemAuditRepository orderItemAuditRepository;

    @Autowired
    public OrderItemAuditService(OrderItemAuditRepository orderItemAuditRepository) {
        this.orderItemAuditRepository = orderItemAuditRepository;
    }

    @Override
    public List<Integer> findTopSellingItemIds() {
        return orderItemAuditRepository.findTopSellingItemCodes();
    }

    @Override
    public Integer totalItemOrderedCount() {
        return Math.toIntExact(orderItemAuditRepository.totalItemOrdered());
    }

    @Override
    public Integer findTotalItemsPackedCount(boolean val) {
        return orderItemAuditRepository.findTotalItemsPacked(val);
    }

    @Override
    public Integer totalOrders(int itemId) {
        return orderItemAuditRepository.totalOrders(itemId);
    }

    @Override
    public OrderItemAudit findAuditById(int id) {
        return orderItemAuditRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Requested Item Audit was not found"));
    }

    @Override
    public void saveAllItemOrders(List<OrderItemAudit> orderItemAudits) {
        orderItemAuditRepository.saveAll(orderItemAudits);

    }

    @Override
    public void saveAudit(OrderItemAudit orderItemAudit) {
        orderItemAuditRepository.save(orderItemAudit);

    }
}
