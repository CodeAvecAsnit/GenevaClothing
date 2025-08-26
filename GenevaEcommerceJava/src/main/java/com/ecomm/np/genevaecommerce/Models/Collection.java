package com.ecomm.np.genevaecommerce.Models;

import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "collection_id")
    private int collectionId;

    private String collectionName;

    @Lob
    private String collection_description;

    @OneToMany (mappedBy = "collection",fetch = FetchType.LAZY)
    private List<Items> collectionItemList;

    @CreatedDate
    private LocalDateTime launchedDate;

    public Collection() {
    }

    public Collection(int collectionId, List<Items> collectionItemList, LocalDateTime launchedDate,String collectionName,
    String collectionDescription) {
        this.collectionId = collectionId;
        this.collectionItemList = collectionItemList;
        this.launchedDate = launchedDate;
        this.collectionName = collectionName;
        this.collection_description = collectionDescription;
    }

    public static List<NewCollectionDTO> emptyList() {
        return null;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public List<Items> getCollectionItemList() {
        return collectionItemList;
    }

    public void setCollectionItemList(List<Items> collectionItemList) {
        this.collectionItemList = collectionItemList;
    }

    public LocalDateTime getLaunchedDate() {
        return launchedDate;
    }

    public void setLaunchedDate(LocalDateTime launchedDate) {
        this.launchedDate = launchedDate;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollection_description() {
        return collection_description;
    }

    public void setCollection_description(String collection_description) {
        this.collection_description = collection_description;
    }
}
