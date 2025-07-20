package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Integer> {
}
