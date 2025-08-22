package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface OrderItemsRepository extends JpaRepository<OrderedItems,Integer> {
    Page<OrderedItems> findByOrderDetails(OrderDetails orderDetails, Pageable page);
    Page<OrderedItems> findByActive(boolean isActive,Pageable page);
}
