package com.ecomm.np.genevaecommerce.dto;

import com.ecomm.np.genevaecommerce.model.Items;

public class ListItemDTO {
    private String itemName;
    private String description;
    private float price;
    private String collection;
    private String gender;
    private int stock;

    public ListItemDTO() {
    }

    public ListItemDTO(String itemName, String description, float price, String collection, String gender, int stock) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.collection = collection;
        this.gender = gender;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {this.gender = gender;
    }

    public static Items ItemsMapper(ListItemDTO listItemDTO,String imageUrl,String imageId){
        Items item = new Items();
        item.setItemName(listItemDTO.getItemName());
        item.setDescription(listItemDTO.getDescription());
        item.setImageLink(imageUrl);
        item.setPrice(listItemDTO.getPrice());
        item.setStock(listItemDTO.getStock());
        item.setImageId(imageId);
        return item;
    }
}
