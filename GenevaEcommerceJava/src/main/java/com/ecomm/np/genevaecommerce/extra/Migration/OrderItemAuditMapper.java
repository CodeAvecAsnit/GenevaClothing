package com.ecomm.np.genevaecommerce.extra.Migration;

/**
 * @author : Asnit Bakhati
 */

public class OrderItemAuditMapper {

    private int orderId;
    private boolean isActive;
    private boolean isPacked;
    private boolean isDelivered;
    private int quantity;
    private String size;
    private float itemPrice;
    private int orderItemId;
    private int itemCode;

    public OrderItemAuditMapper() {
    }

    public OrderItemAuditMapper(int orderId, boolean isActive, boolean isPacked, boolean isDelivered, int quantity, String size, float itemPrice, int orderItemId, int itemCode) {
        this.orderId = orderId;
        this.isActive = isActive;
        this.isPacked = isPacked;
        this.isDelivered = isDelivered;
        this.quantity = quantity;
        this.size = size;
        this.itemPrice = itemPrice;
        this.orderItemId = orderItemId;
        this.itemCode = itemCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String toString() {
        return "OrderItemAuditMapper{" +
                "orderId=" + orderId +
                ", isActive=" + isActive +
                ", isPacked=" + isPacked +
                ", isDelivered=" + isDelivered +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", itemPrice=" + itemPrice +
                ", orderItemId=" + orderItemId +
                ", itemCode=" + itemCode +
                '}';
    }
}
