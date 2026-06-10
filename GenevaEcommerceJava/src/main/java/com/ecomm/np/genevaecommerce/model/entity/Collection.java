package com.ecomm.np.genevaecommerce.model.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private int collectionId;

    private String collectionName;

    @Column
    @Lob
    private String collectionDescription;

    @OneToMany (mappedBy = "collection",fetch = FetchType.LAZY)
    private List<Items> collectionItemList;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime launchedDate;

    public Collection() {
    }

    public Collection(int collectionId, List<Items> collectionItemList, LocalDateTime launchedDate,String collectionName,
    String collectionDescription) {
        this.collectionId = collectionId;
        this.collectionItemList = collectionItemList;
        this.launchedDate = launchedDate;
        this.collectionName = collectionName;
        this.collectionDescription = collectionDescription;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
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

    public void setCollection_description(String collectionDescription) {
        this.collectionDescription = collectionDescription;
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
}
