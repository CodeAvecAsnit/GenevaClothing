package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.entity.Collection;

/**
 * @author : Asnit Bakhati
 */

public interface CollectionService {

    Collection findCollectionById(int id);

    Collection findCollectionByName(String collectionName);

    Collection findLatestCollection();

    void saveCollection(String collectionName,String description);

}
