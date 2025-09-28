package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.Collection;

import java.util.List;

public class CollectionAndItemsDTO {
    private String collectionName;
    private String collectionDescription;
    private List<ItemDisplayDTO> collectionItems;

    public CollectionAndItemsDTO() {
    }

    public CollectionAndItemsDTO(String collectionName, String collectionDescription, List<ItemDisplayDTO> collectionItems) {
        this.collectionName = collectionName;
        this.collectionDescription = collectionDescription;
        this.collectionItems = collectionItems;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionDescription() {
        return collectionDescription;
    }

    public void setCollectionDescription(String collectionDescription) {
        this.collectionDescription = collectionDescription;
    }

    public List<ItemDisplayDTO> getCollectionItems() {
        return collectionItems;
    }

    public void setCollectionItems(List<ItemDisplayDTO> collectionItems) {
        this.collectionItems = collectionItems;
    }

    public static CollectionAndItemsDTO buildFromCollection(Collection collection){
        CollectionAndItemsDTO collectionAndItemsDTO = new CollectionAndItemsDTO();
        collectionAndItemsDTO.setCollectionName(collection.getCollectionName());
        collectionAndItemsDTO.setCollectionDescription(collection.getCollectionDescription());
        List<ItemDisplayDTO> itemDisplayDTOS = collection.getCollectionItemList().stream().map(ItemDisplayDTO::MapByItems).toList();
        collectionAndItemsDTO.setCollectionItems(itemDisplayDTOS);
        return collectionAndItemsDTO;
    }

}

