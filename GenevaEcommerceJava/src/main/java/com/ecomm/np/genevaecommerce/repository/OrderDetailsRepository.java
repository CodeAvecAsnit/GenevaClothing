package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : Asnit Bakhati
 */

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Integer> {
}
