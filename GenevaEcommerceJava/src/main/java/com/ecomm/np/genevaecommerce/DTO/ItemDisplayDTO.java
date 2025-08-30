package com.ecomm.np.genevaecommerce.dto;

import com.ecomm.np.genevaecommerce.model.Items;

public class ItemDisplayDTO {
    private int id;
    private String ItemName;
    private String description;
    private float price;
    private String imageLink;

    public ItemDisplayDTO(){
    }

    public ItemDisplayDTO(int id, String itemName, String description, float price, String imageLink) {
        this.id = id;
        ItemName = itemName;
        this.description = description;
        this.price = price;
        this.imageLink = imageLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public static ItemDisplayDTO MapByItems(Items items){
        return new ItemDisplayDTO(items.getItemCode(), items.getItemName(),items.getDescription(),items.getPrice(), items.getImageLink());
    }
}
