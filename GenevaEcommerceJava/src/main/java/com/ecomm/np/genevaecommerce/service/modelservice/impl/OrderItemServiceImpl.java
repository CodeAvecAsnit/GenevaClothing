package com.ecomm.np.genevaecommerce.service.modelservice.impl;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.repository.OrderItemsRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemsRepository orderItemsRepository;

    public OrderItemServiceImpl(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    @Transactional
    public Page<OrderedItems> findByOrderDetails(OrderDetails orderDetails, Pageable pageable) {
        return orderItemsRepository.findByOrderDetails(orderDetails,pageable);
    }

    @Override
    @Transactional
    public OrderedItems findOrderedItemsById(int oId) {
        return orderItemsRepository.findById(oId).orElseThrow(()->
                new ResourceNotFoundException("The requested order was not found"));
    }

    @Override
    @Transactional
    public Page<OrderedItems> findAllOrdered(Pageable pageable) {
        return orderItemsRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<OrderedItems> findActiveOrderedItems(Pageable pageable, boolean isActive) {
        return orderItemsRepository.findByMainActive(isActive,pageable);
    }

    @Override
    @Transactional
    public OrderedItems saveOrderedItems(OrderedItems orderedItems) {
        return orderItemsRepository.save(orderedItems);
    }

    @Override
    @Transactional
    public Integer countAllItems() {
        return (int) orderItemsRepository.count();
    }

    @Override
    @Transactional
    public Long findNotDeliveredCount(boolean delivered) {
        return orderItemsRepository.findNotDelivered(delivered);
    }

    @Override
    @Transactional
    public Long findTotalOrdersTodayCount() {
        return orderItemsRepository.findTotalOrdersToday();
    }

    @Override
    @Transactional
    public Long findNotPackedCount(boolean packed) {
        return orderItemsRepository.findNotPacked(packed);
    }


    @Override
    @Transactional
    public Float findTotalSalesCount() {
        return orderItemsRepository.findTotalSales();
    }

    @Override
    @Transactional
    public Float findTotalSalesTodayCount() {
        return orderItemsRepository.findTotalSalesToday();
    }

    @Override
    @Transactional
    public List<Object[]> findOrderCountLast7DaysCount() {
        return orderItemsRepository.findOrderCountLast7Days();
    }
}
