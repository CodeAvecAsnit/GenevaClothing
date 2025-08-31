package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderedItems,Integer> {
    Page<OrderedItems> findByOrderDetails(OrderDetails orderDetails, Pageable page);
    Page<OrderedItems> findByMainActive(boolean mainActive, Pageable page);


    @Query(value = """
    SELECT COUNT(o_id) FROM ordered_items
    WHERE order_initiated_date >= CURDATE()
    AND order_initiated_date < CURDATE() + INTERVAL 1 DAY
    """, nativeQuery = true)
    Long findTotalOrdersToday();

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM ordered_items", nativeQuery = true)
    Float findTotalSales();

    @Query(value = """
    SELECT COALESCE(SUM(total_price), 0) FROM ordered_items
    WHERE order_initiated_date >= CURDATE()
    AND order_initiated_date < CURDATE() + INTERVAL 1 DAY
    """, nativeQuery = true)
    Float findTotalSalesToday();

    @Query(value = "SELECT COUNT(o_id) FROM ordered_items WHERE main_active = :val", nativeQuery = true)
    Long findNotDelivered(@Param("val") boolean isActive);

    @Query(value = "SELECT COUNT(o_id) FROM ordered_items WHERE processed = :val", nativeQuery = true)
    Long findNotPacked(@Param("val") boolean isActive);

    @Query(value = """
    SELECT DATE(order_initiated_date) AS order_date,
           COUNT(*) AS total_orders
    FROM ordered_items
    WHERE order_initiated_date >= CURDATE() - INTERVAL 6 DAY
      AND order_initiated_date < CURDATE() + INTERVAL 1 DAY
    GROUP BY DATE(order_initiated_date)
    ORDER BY order_date ASC
    """, nativeQuery = true)
    List<Object[]> findOrderCountLast7Days();
}
