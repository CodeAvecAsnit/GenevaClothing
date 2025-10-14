package com.ecomm.np.genevaecommerce.service.modelservice.impl;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.repository.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Asnit Bakhati
 */

@Service
public class OrderItemAuditServiceImpl implements OrderItemAuditService {

    private final OrderItemAuditRepository orderItemAuditRepository;

    @Autowired
    public OrderItemAuditServiceImpl(OrderItemAuditRepository orderItemAuditRepository) {
        this.orderItemAuditRepository = orderItemAuditRepository;
    }

    @Override
    @Transactional
    public List<Integer> findTopSellingItemIds() {
        return orderItemAuditRepository.findTopSellingItemCodes();
    }

    @Override
    @Transactional
    public Integer totalItemOrderedCount() {
        return Math.toIntExact(orderItemAuditRepository.totalItemOrdered());
    }

    @Override
    @Transactional
    public Integer findTotalItemsPackedCount(boolean val) {
        return orderItemAuditRepository.findTotalItemsPacked(val);
    }

    @Override
    @Transactional
    public Integer totalOrders(int itemId) {
        return orderItemAuditRepository.totalOrders(itemId);
    }

    @Override
    @Transactional
    public OrderItemAudit findAuditById(int id) {
        return orderItemAuditRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Requested Item Audit was not found"));
    }

    @Override
    @Transactional
    public void saveAllItemOrders(List<OrderItemAudit> orderItemAudits) {
        orderItemAuditRepository.saveAll(orderItemAudits);
    }
    @Override
    @Transactional
    public void saveAudit(OrderItemAudit orderItemAudit) {
        orderItemAuditRepository.save(orderItemAudit);
    }
}
