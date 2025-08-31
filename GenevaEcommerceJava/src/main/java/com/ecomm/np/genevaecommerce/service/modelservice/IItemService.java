package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.GenderTable;
import com.ecomm.np.genevaecommerce.model.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IItemService {

    Items findItemById(int id);

    void saveItem(Items item);

    Page<Items> findGenderItems(GenderTable genderTable, Pageable pageable);

    Page<Items> findAllTheItems(Pageable page);

    List<Items> findTop10();

    Long findTotalItemCount();

    List<Items> findAllByListOfIds(List<Integer> ids);
}
