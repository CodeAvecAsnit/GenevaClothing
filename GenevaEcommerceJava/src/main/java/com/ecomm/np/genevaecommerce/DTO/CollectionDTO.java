package com.ecomm.np.genevaecommerce.dto;

import com.ecomm.np.genevaecommerce.model.Collection;

public class CollectionDTO {
   private String imageLink;
   private String collectionName;
   private String collectionDescription;

    public CollectionDTO(String imageLink, String collectionName, String collectionDescription) {
        this.imageLink = imageLink;
        this.collectionName = collectionName;
        this.collectionDescription = collectionDescription;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

    public static CollectionDTO buildFromCollection(Collection c,String ImageLink){
        return new CollectionDTO(ImageLink, c.getCollectionName(), c.getCollection_description());
    }
}
