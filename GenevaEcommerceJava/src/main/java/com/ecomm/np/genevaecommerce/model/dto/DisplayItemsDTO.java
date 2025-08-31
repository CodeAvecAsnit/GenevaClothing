package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;

public class DisplayItemsDTO {
    private int itemCode;
    private int itemOrderCode;
    private String itemName;
    private String imageLink;
    private float price;
    private int quantity;
    private String size;
    private float totalItemPrice;
    private boolean packed;

    public DisplayItemsDTO() {
    }

    public DisplayItemsDTO(int itemCode, int itemOrderCode, String itemName, String imageLink, float price, int quantity, String size, float totalItemPrice, boolean packed) {
        this.itemCode = itemCode;
        this.itemOrderCode = itemOrderCode;
        this.itemName = itemName;
        this.imageLink = imageLink;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.totalItemPrice = totalItemPrice;
        this.packed = packed;
    }

    public DisplayItemsDTO(Items item, int quantity, String size){
        this.itemCode=item.getItemCode();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.quantity=quantity;
        this.imageLink= item.getImageLink();
        float rawTotal = this.quantity * this.price;
        this.size = size;
        this.totalItemPrice = (float) Math.ceil(rawTotal * 100) / 100;

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

    public int getItemOrderCode() {return itemOrderCode;
    }

    public void setItemOrderCode(int itemOrderCode) {
        this.itemOrderCode = itemOrderCode;
    }


    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public static DisplayItemsDTO buildFromOrderAudit(OrderItemAudit audit){
        DisplayItemsDTO dto = new DisplayItemsDTO();
        Items item = audit.getItem();
        dto.setItemName(item.getItemName());
        dto.setImageLink(item.getImageLink());
        dto.setItemCode(item.getItemCode());
        dto.setPrice(item.getPrice());
        dto.setQuantity(audit.getQuantity());
        dto.setSize(audit.getSize());
        dto.setTotalItemPrice(audit.getItemPrice());
        dto.setItemOrderCode(audit.getOrderTracerCode());
        dto.setPacked(audit.isPacked());
        return dto;
    }
}
