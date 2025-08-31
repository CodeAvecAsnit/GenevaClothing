package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.repository.CollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionService implements ICollectionService{

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public Collection findCollectionById(int id) {
        return collectionRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Requested Collection was not found"));
    }

    @Override
    public Collection findCollectionByName(String collectionName) {
            return collectionRepository.findByCollectionName(collectionName).
                    orElseThrow(() -> new ResourceNotFoundException("Cannot find the collection"));

    }

    @Override
    public Collection findLatestCollection() {
        return collectionRepository.findTopByOrderByLaunchedDateDesc();
    }

    @Override
    public void saveCollection(String collectionName,String description) {
        Collection collection = new Collection();
        collection.setCollectionName(collectionName);
        collection.setCollection_description(description);
        collectionRepository.save(collection);
    }

    @Override
    public void saveCollection(Collection collection){
        collectionRepository.save(collection);
    }
}
