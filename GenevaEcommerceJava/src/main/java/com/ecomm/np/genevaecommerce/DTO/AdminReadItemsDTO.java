package com.ecomm.np.genevaecommerce.dto;

import com.ecomm.np.genevaecommerce.enumeration.Gender;
import com.ecomm.np.genevaecommerce.model.Items;

public class AdminReadItemsDTO {

    private int itemId; //readonly
    private String itemName;
    private String itemDescription;
    private String imageUrl;
    private String imageId;
    private float price;
    private int stock;
    private String collection;
    private String createdDate;//readonly
    private String updatedDate;//readonly handled by server
    private Gender gender;
    private int wishCount;//readonly
    private int cartCount;//readonly
    private int totalOrders;//readonly

    public AdminReadItemsDTO() {
    }

    public AdminReadItemsDTO(int itemId, String itemName, String itemDescription, String imageUrl, String imageId, float price, int stock, String collection, String createdDate, String updatedDate, Gender gender, int wishCount, int cartCount, int totalOrders) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.price = price;
        this.stock = stock;
        this.collection = collection;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.gender = gender;
        this.wishCount = wishCount;
        this.cartCount = cartCount;
        this.totalOrders = totalOrders;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getWishCount() {
        return wishCount;
    }

    public void setWishCount(int wishCount) {
        this.wishCount = wishCount;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public static AdminReadItemsDTO buildFromItem(Items item){
        AdminReadItemsDTO admin = new AdminReadItemsDTO();
        admin.setItemId(item.getItemCode());
        admin.setItemName(item.getItemName());
        admin.setItemDescription(item.getDescription());
        admin.setImageId(item.getImageId());
        admin.setImageUrl(item.getImageLink());
        admin.setPrice(item.getPrice());
        admin.setStock(item.getStock());
        return admin;
    }
}
