package com.ecomm.np.genevaecommerce.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "collection_id")
    private int collectionId;

    private String collectionName;

    @OneToMany (mappedBy = "collection")
    private List<Items> collectionItemList;

    @UpdateTimestamp
    private LocalDateTime launchedDate;

    public Collection() {
    }

    public Collection(int collectionId, List<Items> collectionItemList, LocalDateTime launchedDate,String collectionName) {
        this.collectionId = collectionId;
        this.collectionItemList = collectionItemList;
        this.launchedDate = launchedDate;
        this.collectionName = collectionName;
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
}
