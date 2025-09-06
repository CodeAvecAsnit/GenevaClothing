package com.ecomm.np.genevaecommerce.service.modelservice.impl;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.repository.CollectionRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.CollectionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    @Transactional
    public Collection findCollectionById(int id) {
        return collectionRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Requested Collection was not found"));
    }

    @Override
    @Transactional
    public Collection findCollectionByName(String collectionName) {
            return collectionRepository.findByCollectionName(collectionName).
                    orElseThrow(() -> new ResourceNotFoundException("Cannot find the collection"));
    }

    @Override
    @Transactional
    public Collection findLatestCollection() {
        return collectionRepository.findTopByOrderByLaunchedDateDesc();
    }

    @Override
    @Transactional
    public void saveCollection(String collectionName,String description) {
        Collection collection = new Collection();
        collection.setCollectionName(collectionName);
        collection.setCollection_description(description);
        collectionRepository.save(collection);
    }

    @Override
    @Transactional
    public void saveCollection(Collection collection){
        collectionRepository.save(collection);
    }
}
