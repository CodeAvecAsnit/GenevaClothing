package com.ecomm.np.genevaecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@SQLDelete(sql = "UPDATE items SET deleted = true WHERE item_code = ?")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_code")
    private int itemCode;

    private String itemName;

    @Lob
    private String description;

    private String imageLink;

    private String imageId;

    private float price;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonIgnore
    private Collection collection;

    private int stock;

    private boolean deleted=false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    @JsonIgnore
    private GenderTable genderTable;

    @ManyToMany(mappedBy = "wishList",fetch = FetchType.LAZY )
    @JsonIgnore
    private Set<UserModel> wishedUsers;

    @ManyToMany(mappedBy = "cartList",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserModel> cartUsers;

    @OneToMany(fetch = FetchType.LAZY,mappedBy ="items")
    @JsonIgnore
    private List<OrderItemAudit> orderItemAudit;




    public Items(boolean deleted) {
        this.deleted = deleted;
    }

    public Items(String itemName, String description, String imageLink){
        this.itemName = itemName;
        this.description = description;
        this.imageLink = imageLink;
    }

    public Items() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public GenderTable getGenderTable() {
        return genderTable;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Set<UserModel> getCartUsers() {
        return cartUsers;
    }

    public void setCartUsers(Set<UserModel> cartUsers) {
        this.cartUsers = cartUsers;
    }

    public Set<UserModel> getWishedUsers() {
        return wishedUsers;
    }

    public void setWishedUsers(Set<UserModel> wishedUsers) {
        this.wishedUsers = wishedUsers;
    }

    public void setGenderTable(GenderTable genderTable) {
        this.genderTable = genderTable;
    }

    public List<OrderItemAudit> getOrderItemAudit() {
        return orderItemAudit;
    }

    public void setOrderItemAudit(List<OrderItemAudit> orderItemAudit) {
        this.orderItemAudit = orderItemAudit;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
