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
public interface ItemsRepository extends JpaRepository<Items,Integer> {
    List<Items> findTop10ByOrderByCreatedDateDesc();

    Page<Items> findByGenderTable(GenderTable genderTable, Pageable pageable);

/*    A procedure that calculates the price according for each quantity
    delimiter $$

    Create procedure findTotalPrice(in quantity int,in id int)
    begin
    declare item_price decimal(10,2);
    declare item_stock int;
    declare total_price decimal(10,2);

    select price,stock into item_price,item_stock
    from items
    where item_code = id;

    if(item_stock<quantity) then
    set total_price = 0;
    else
    set total_price = item_price*quantity;
    end if;

    select total_price;
    end$$*/
    @Query(value="Call findTotalPrice(:quantity,:id)",nativeQuery = true)
    Float findTotalPrice(@Param("quantity")int quantity,@Param("id")int id);

    @Query(value = "select count(item_code) from items",nativeQuery = true)
    Long findTotalItems();
}
