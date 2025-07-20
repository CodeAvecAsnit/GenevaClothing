package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderedItems,Integer> {
}
