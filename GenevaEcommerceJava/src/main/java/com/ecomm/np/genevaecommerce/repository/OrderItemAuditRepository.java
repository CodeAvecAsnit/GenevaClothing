package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemAuditRepository extends JpaRepository<OrderItemAudit,Integer> {

    @Query(value = "Select coalesce(sum(quantity),0) from " +
            "order_item_audit where item_code = :itemCode",
            nativeQuery = true)
    Integer totalOrders(@Param("itemCode")int id);

    @Query(value = "Select coalesce(sum(quantity),0) from " +
            "order_item_audit",
            nativeQuery = true)
    Integer totalItemOrdered();


    @Query(
            value = "SELECT item_code " +
                    "FROM order_item_audit " +
                    "GROUP BY item_code " +
                    "ORDER BY SUM(quantity) DESC " +
                    "LIMIT 5",
            nativeQuery = true
    )
    List<Integer> findTopSellingItemCodes();

    @Query(value = "select coalesce(sum(quantity),0)from order_item_audit where is_packed=:val" , nativeQuery = true)
    Integer findTotalItemsPacked(@Param("val") boolean packed);
}
