package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : Asnit Bakhati
 */
@Repository
public interface CollectionRepository extends JpaRepository<Collection,Integer> {
    Optional<Collection> findByCollectionName(String collectionName);
    Collection findTopByOrderByLaunchedDateDesc();
}
