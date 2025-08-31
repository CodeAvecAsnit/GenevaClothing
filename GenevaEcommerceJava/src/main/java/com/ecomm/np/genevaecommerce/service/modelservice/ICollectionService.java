package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.Collection;

public interface ICollectionService {

    Collection findCollectionById(int id);

    Collection findCollectionByName(String collectionName);

    Collection findLatestCollection();

    void saveCollection(Collection collection);

    void saveCollection(String collectionName,String description);

}
