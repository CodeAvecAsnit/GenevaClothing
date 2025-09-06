package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemService {
    Page<OrderedItems> findByOrderDetails(OrderDetails orderDetails, Pageable pageable);
    OrderedItems findOrderedItemsById(int oId);
    Page<OrderedItems> findAllOrdered(Pageable pageable);
    Page<OrderedItems> findActiveOrderedItems(Pageable pageable,boolean isActive);
    void saveOrderedItems(OrderedItems orderedItems);
    Integer countAllItems();
    Long findNotDeliveredCount(boolean delivered);
    Long findTotalOrdersTodayCount();
    Long findNotPackedCount(boolean packed);
    Float findTotalSalesCount();
    Float findTotalSalesTodayCount();
    List<Object[]> findOrderCountLast7DaysCount();
}
