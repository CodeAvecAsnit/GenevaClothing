package com.ecomm.np.genevaecommerce.TestPackage.Encryption;

public class OrderData {
    private int orderId;
    private int totalAmount;
    private int paidAmount;
    private int noOfItems;
    private String address;
    private String payload;

    public OrderData() {
    }

    public OrderData(int orderId, int totalAmount, int paidAmount, int noOfItems, String address, String payload) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.noOfItems = noOfItems;
        this.address = address;
        this.payload = payload;
    }

    public OrderData(int orderId, int totalAmount, int paidAmount, int noOfItems, String address) {
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

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
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
}
