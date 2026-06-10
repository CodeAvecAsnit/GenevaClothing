package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.enumeration.Gender;
import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : Asnit Bakhati
 */

@Repository
public interface GenderTableRepository extends JpaRepository<GenderTable,Integer> {
    Optional<GenderTable> findByGender(Gender gender);
}
