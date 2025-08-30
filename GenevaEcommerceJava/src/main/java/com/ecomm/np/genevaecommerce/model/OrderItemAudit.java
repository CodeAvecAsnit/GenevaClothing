package com.ecomm.np.genevaecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
public class OrderItemAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderTracerCode;

    private boolean isActive;

    private boolean isPacked;

    private boolean isDelivered;

    private int quantity;

    private String size;

    private float itemPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderedItems orderedItems;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Items items;

    public OrderItemAudit(int orderTracerCode, boolean isActive, boolean isPacked, boolean isDelivered, int quantity, String size, float itemPrice, OrderedItems orderedItems, Items items) {
        this.orderTracerCode = orderTracerCode;
        this.isActive = isActive;
        this.isPacked = isPacked;
        this.isDelivered = isDelivered;
        this.quantity = quantity;
        this.size = size;
        this.itemPrice = itemPrice;
        this.orderedItems = orderedItems;
        this.items = items;
    }

    public OrderItemAudit() {

    }

    public int getOrderTracerCode() {
        return orderTracerCode;
    }

    public void setOrderTracerCode(int orderTracerCode) {
        this.orderTracerCode = orderTracerCode;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public OrderedItems getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(OrderedItems orderedItems) {
        this.orderedItems = orderedItems;
    }

    public Items getItem() {
        return items;
    }

    public void setItem(Items item) {
        this.items = item;
    }

    public float setTotalPrice(){
        itemPrice = quantity * items.getPrice();
        return itemPrice;
    }
}
