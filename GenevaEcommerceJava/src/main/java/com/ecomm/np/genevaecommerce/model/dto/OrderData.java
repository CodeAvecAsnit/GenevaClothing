package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

public class OrderData {
    private int orderId;
    private float totalAmount;
    private float paidAmount;
    private int noOfItems;
    private String address;
    private String payload;

    public OrderData() {
    }

    public OrderData(int orderId, float totalAmount, float paidAmount, int noOfItems, String address, String payload) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.noOfItems = noOfItems;
        this.address = address;
        this.payload = payload;
    }

    public OrderData(int orderId, float totalAmount, float paidAmount, int noOfItems, String address) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.noOfItems = noOfItems;
        this.address = address;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "{" +
                "  \"orderId\": " + orderId + "," +
                "  \"totalAmount\": " + totalAmount + "," +
                "  \"paidAmount\": " + paidAmount + "," +
                "  \"noOfItems\": " + noOfItems + "," +
                "  \"address\": \"" + escapeJson(address) + "\"" +
                "}";
    }

    public String allToString() {
        return "{" +
                "  \"orderId\": " + orderId + "," +
                "  \"totalAmount\": " + totalAmount + "," +
                "  \"paidAmount\": " + paidAmount + "," +
                "  \"noOfItems\": " + noOfItems + "," +
                "  \"address\": \"" + escapeJson(address) + "\"," +
                " \"payload\":\""+payload+"\""+
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static OrderData buildFromOrderedItems(OrderedItems or){
        OrderData orderData = new OrderData();
        orderData.setOrderId(or.getoId());
        orderData.setPaidAmount(or.getPaidPrice().floatValue());
        orderData.setTotalAmount(or.getTotalPrice().floatValue());
        orderData.setNoOfItems(or.getOrderItemAuditList().size());
        OrderDetails od = or.getOrderDetails();
        StringBuilder address = new StringBuilder(od.getDeliveryLocation());
        address.append(" , ");
        address.append(od.getCity());
        address.append(" , ");
        address.append(od.getProvince());
        orderData.setAddress(address.toString());
        return orderData;
    }
}
