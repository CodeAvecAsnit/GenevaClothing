package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemAuditRepository extends JpaRepository<OrderItemAudit, Integer> {

    //Total orders for a given item code
    @Query(value = """
        SELECT COALESCE(SUM(quantity), 0)::int
        FROM order_item_audit
        WHERE item_code = :itemCode
        """, nativeQuery = true)
    Integer totalOrders(@Param("itemCode") int id);

    //Total items ordered (all codes)
    @Query(value = """
        SELECT COALESCE(SUM(quantity), 0)::int
        FROM order_item_audit
        """, nativeQuery = true)
    Integer totalItemOrdered();

    // Top 5 selling item codes
    @Query(value = """
        SELECT item_code
        FROM order_item_audit
        GROUP BY item_code
        ORDER BY SUM(quantity) DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Integer> findTopSellingItemCodes();

    // Total items packed (boolean works natively in Postgres)
    @Query(value = """
        SELECT COALESCE(SUM(quantity), 0)::int
        FROM order_item_audit
        WHERE is_packed = :val
        """, nativeQuery = true)
    Integer findTotalItemsPacked(@Param("val") boolean packed);
}

