package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Models.GenderTable;
import com.ecomm.np.genevaecommerce.Models.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items,Integer> {
    List<Items> findTop10ByOrderByCreatedDateDesc();

    Page<Items> findByGenderTable(GenderTable genderTable, Pageable pageable);
}
