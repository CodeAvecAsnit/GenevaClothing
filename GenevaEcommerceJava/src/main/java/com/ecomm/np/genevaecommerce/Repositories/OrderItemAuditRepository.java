package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Models.OrderItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemAuditRepository extends JpaRepository<OrderItemAudit,Integer> {
}
