package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.OrderDetails;
import com.ecomm.np.genevaecommerce.model.OrderedItems;
import com.ecomm.np.genevaecommerce.repository.OrderItemsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService implements IOrderItemService{

    private final OrderItemsRepository orderItemsRepository;

    public OrderItemService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public Page<OrderedItems> findByOrderDetails(OrderDetails orderDetails, Pageable pageable) {
        return orderItemsRepository.findByOrderDetails(orderDetails,pageable);
    }

    @Override
    public OrderedItems findOrderedItemsById(int oId) {
        return orderItemsRepository.findById(oId).orElseThrow(()->
                new ResourceNotFoundException("The requested order was not found"));
    }

    @Override
    public Page<OrderedItems> findAllOrdered(Pageable pageable) {
        return orderItemsRepository.findAll(pageable);
    }

    @Override
    public Page<OrderedItems> findActiveOrderedItems(Pageable pageable, boolean isActive) {
        return orderItemsRepository.findByMainActive(isActive,pageable);
    }

    @Override
    public void saveOrderedItems(OrderedItems orderedItems) {
        orderItemsRepository.save(orderedItems);
    }

    @Override
    public Integer countAllItems() {
        return (int) orderItemsRepository.count();
    }

    @Override
    public Long findNotDeliveredCount(boolean delivered) {
        return orderItemsRepository.findNotDelivered(delivered);
    }

    @Override
    public Long findTotalOrdersTodayCount() {
        return orderItemsRepository.findTotalOrdersToday();
    }

    @Override
    public Long findNotPackedCount(boolean packed) {
        return orderItemsRepository.findNotPacked(packed);
    }


    @Override
    public Float findTotalSalesCount() {
        return orderItemsRepository.findTotalSales();
    }

    @Override
    public Float findTotalSalesTodayCount() {
        return orderItemsRepository.findTotalSalesToday();
    }

    @Override
    public List<Object[]> findOrderCountLast7DaysCount() {
        return orderItemsRepository.findOrderCountLast7Days();
    }
}
