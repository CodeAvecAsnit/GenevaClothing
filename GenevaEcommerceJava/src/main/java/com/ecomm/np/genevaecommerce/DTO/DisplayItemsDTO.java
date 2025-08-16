package com.ecomm.np.genevaecommerce.DTO;

import com.ecomm.np.genevaecommerce.Models.Items;

public class DisplayItemsDTO {
    private int itemId;
    private String ItemName;
    private float price;
    private int quantity;
    private float totalItemPrice;

    public DisplayItemsDTO() {
    }

    public DisplayItemsDTO(int itemId, String itemName, float price, int quantity, float totalItemPrice) {
        this.itemId = itemId;
        ItemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.totalItemPrice = totalItemPrice;
    }

    public DisplayItemsDTO(Items item, int quantity){
        this.itemId=item.getItemCode();
        this.ItemName = item.getItemName();
        this.price = item.getPrice();
        this.quantity=quantity;
        float rawTotal = this.quantity * this.price;
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
}
