package com.ecomm.np.genevaecommerce.services;

import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.Enumerations.Gender;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Repositories.CollectionRepository;
import com.ecomm.np.genevaecommerce.Repositories.GenderTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class AdminItemService {

    private static final Logger log = LogManager.getLogger(AdminItemService.class);
    private final CloudinaryService cloudinaryService;
    private final ItemsRepository itemsRepository;
    private final CollectionRepository collectionRepository;
    private final GenderTableRepository genderTableRepository;

    @Autowired
    public AdminItemService(CloudinaryService cloudinaryService, ItemsRepository itemsRepository, CollectionRepository collectionRepository, GenderTableRepository genderTableRepository) {
        this.cloudinaryService = cloudinaryService;
        this.itemsRepository = itemsRepository;
        this.collectionRepository = collectionRepository;
        this.genderTableRepository = genderTableRepository;
    }

    private Collection findCollectionByName(String name) {
        return collectionRepository.findByCollectionName(name).orElseThrow(() -> new ResourceNotFoundException("Cannot find the collection"));
    }


    public Items saveItem(ListItemDTO item,MultipartFile file) throws Exception {
        String imageUrl = uploadImage(file);
        Items newItem = ListItemDTO.ItemsMapper(item, imageUrl);
        setCollectionIfExists(newItem, item.getCollection());
        setGenderIfValid(newItem, item.getGender());
        return itemsRepository.save(newItem);
    }

    private String uploadImage(MultipartFile file) throws RuntimeException {
        try {
            Map<?, ?> imageDescription = cloudinaryService.upload(file);
            return (String) imageDescription.get("url");
        } catch (RuntimeException ex) {
            log.error("Image upload failed: {}", ex.getMessage());
            throw ex;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCollectionIfExists(Items newItem, String collectionName) {
        try {
            Collection collection = findCollectionByName(collectionName);
            newItem.setCollection(collection);
        } catch (Exception e) {
            log.warn("Collection not found: {}", collectionName);
        }
    }

    private void setGenderIfValid(Items newItem, String genderStr) {
        try {
            Gender gender = Gender.valueOf(genderStr);
            genderTableRepository.findByGender(gender)
                    .ifPresent(newItem::setGenderTable);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid gender '{}': {}", genderStr, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error setting gender: {}", ex.getMessage());
        }
    }
}
