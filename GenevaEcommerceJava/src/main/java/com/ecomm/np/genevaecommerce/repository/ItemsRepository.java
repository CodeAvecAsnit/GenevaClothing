package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer> {

    List<Items> findTop10ByOrderByCreatedDateDesc();


    Page<Items> findByGenderTable(GenderTable genderTable, Pageable pageable);

    @Query(value="SELECT findTotalPrice(:quantity, :id)", nativeQuery = true)
    Float findTotalPrice(@Param("quantity") int quantity, @Param("id") int id);


    @Query(value = "SELECT COUNT(item_code) FROM items", nativeQuery = true)
    Long findTotalItems();

    @Query(value = "SELECT * FROM items ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<Items> findRandomItems();
}

