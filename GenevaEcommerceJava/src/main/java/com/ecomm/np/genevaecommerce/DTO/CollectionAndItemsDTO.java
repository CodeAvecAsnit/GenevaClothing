package com.ecomm.np.genevaecommerce.dto;

import com.ecomm.np.genevaecommerce.model.Collection;
import com.ecomm.np.genevaecommerce.model.Items;

import java.util.ArrayList;
import java.util.List;

public class CollectionAndItemsDTO {
    private String collectionName;
    private String collectionDescription;
    private List<NewCollectionDTO> collectionItems;

    public CollectionAndItemsDTO() {
    }

    public CollectionAndItemsDTO(String collectionName, String collectionDescription, List<NewCollectionDTO> collectionItems) {
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

    public List<NewCollectionDTO> getCollectionItems() {
        return collectionItems;
    }

    public void setCollectionItems(List<NewCollectionDTO> collectionItems) {
        this.collectionItems = collectionItems;
    }

    public static CollectionAndItemsDTO buildFromCollection(Collection collection){
        CollectionAndItemsDTO collectionAndItemsDTO = new CollectionAndItemsDTO();
        collectionAndItemsDTO.setCollectionName(collection.getCollectionName());
        collectionAndItemsDTO.setCollectionDescription(collection.getCollection_description());
        List<NewCollectionDTO> collectionList = new ArrayList<>();
        for(Items item : collection.getCollectionItemList()){
            NewCollectionDTO newCollectionDTO = NewCollectionDTO.buildFromItem(item);
            collectionList.add(newCollectionDTO);
        }
        collectionAndItemsDTO.setCollectionItems(collectionList);
        return collectionAndItemsDTO;
    }

}

