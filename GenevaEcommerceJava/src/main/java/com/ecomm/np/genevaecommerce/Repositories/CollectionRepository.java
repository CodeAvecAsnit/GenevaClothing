package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Models.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {
    Optional<Collection> findByCollectionName(String collectionName);
    Collection findTopByOrderByLaunchedDateDesc();
}
