package com.ecomm.np.genevaecommerce.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_code")
    private int itemCode;

    private String itemName;

    @Lob
    private String description;

    private String imageLink;

    private float price;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonIgnore
    private Collection collection;

    private int stock;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    @JsonIgnore
    private GenderTable genderTable;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<OrderedItems> orderedItems;

    @ManyToMany(mappedBy = "wishList")
    @JsonIgnore
    private Set<UserModel> wishedUsers;

    @ManyToMany(mappedBy = "cartList")
    @JsonIgnore
    private Set<UserModel> cartUsers;




    public Items() {
    }

    public Items(String itemName,String description,String imageLink){
        this.itemName = itemName;
        this.description = description;
        this.imageLink = imageLink;
    }

    public Items(int itemCode, String itemName, String description, String imageLink, float price, Collection collection, int stock, LocalDateTime createdDate, LocalDateTime updatedDate, GenderTable genderTable, List<OrderedItems> orderedItems, Set<UserModel> wishedUsers, Set<UserModel> cartUsers) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.description = description;
        this.imageLink = imageLink;
        this.price = price;
        this.collection = collection;
        this.stock = stock;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.genderTable = genderTable;
        this.orderedItems = orderedItems;
        this.wishedUsers = wishedUsers;
        this.cartUsers = cartUsers;
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

    public List<OrderedItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItems> orderedItems) {
        this.orderedItems = orderedItems;
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
}
