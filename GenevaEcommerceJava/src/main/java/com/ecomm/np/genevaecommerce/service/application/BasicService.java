package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.entity.BestCollection;
import com.ecomm.np.genevaecommerce.model.entity.Collection;

import java.io.IOException;
import java.util.List;

/**
 * @author : Asnit Bakhati
 */

public interface BasicService {

    Collection saveCollection();

    BestCollection bestCollection() throws IOException;

    List<ItemDisplayDTO> findHighestSellingItems();

    List<ItemDisplayDTO> findRandomItems();

    List<ItemDisplayDTO> displayNewArrivals();
}
