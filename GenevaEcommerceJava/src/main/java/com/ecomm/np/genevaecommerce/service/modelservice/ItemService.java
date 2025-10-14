package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author : Asnit Bakhati
 */

public interface ItemService {

    Items findItemById(int id);

    void saveItem(Items item);

    Page<Items> findGenderItems(GenderTable genderTable, Pageable pageable);

    Page<Items> findAllTheItems(Pageable page);

    List<Items> findTop10();

    Long findTotalItemCount();

    List<Items> findAllByListOfIds(List<Integer> ids);

    List<Items> getRandomItems();

    List<Items> findAllById(List<Integer> ids);
}
