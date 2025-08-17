package com.ecomm.np.genevaecommerce.DTO;

import com.ecomm.np.genevaecommerce.Models.Items;

public class DisplayItemsDTO {
    private int itemId;
    private String ItemName;
    private String imageLink;
    private float price;
    private int quantity;
    private String size;
    private float totalItemPrice;

    public DisplayItemsDTO() {
    }

    public DisplayItemsDTO(int itemId, String itemName, String imageLink, float price, int quantity, String size, float totalItemPrice) {
        this.itemId = itemId;
        ItemName = itemName;
        this.imageLink = imageLink;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.totalItemPrice = totalItemPrice;
        System.out.println("This"+this.imageLink);
    }

    public DisplayItemsDTO(Items item, int quantity, String size){
        this.itemId=item.getItemCode();
        this.ItemName = item.getItemName();
        this.price = item.getPrice();
        this.quantity=quantity;
        this.imageLink= item.getImageLink();
        float rawTotal = this.quantity * this.price;
        this.size = size;
        this.totalItemPrice = (float) Math.ceil(rawTotal * 100) / 100;

    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(float totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
