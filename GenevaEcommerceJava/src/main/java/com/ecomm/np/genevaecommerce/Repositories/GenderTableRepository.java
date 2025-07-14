package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Enumerations.Gender;
import com.ecomm.np.genevaecommerce.Models.GenderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderTableRepository extends JpaRepository<GenderTable,Integer> {
    Optional<GenderTable> findByGender(Gender gender);
}
