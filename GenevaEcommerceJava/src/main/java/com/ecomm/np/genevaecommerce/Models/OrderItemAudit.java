package com.ecomm.np.genevaecommerce.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItemAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderTracerCode;

    private boolean isActive;

    private boolean isPacked;

    private boolean isDelivered;

    private int quantity;

    private float itemPrice;

    @ManyToOne
    private OrderedItems orderedItems;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "item_code")
    private Items items;

    public OrderItemAudit(int orderTracerCode, boolean isActive, boolean isPacked, boolean isDelivered, int quantity, float itemPrice, OrderedItems orderedItems, Items item) {
        this.orderTracerCode = orderTracerCode;
        this.isActive = isActive;
        this.isPacked = isPacked;
        this.isDelivered = isDelivered;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.orderedItems = orderedItems;
        this.items = item;
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
