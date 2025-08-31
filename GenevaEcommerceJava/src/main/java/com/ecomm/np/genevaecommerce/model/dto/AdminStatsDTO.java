package com.ecomm.np.genevaecommerce.model.dto;

import java.util.List;

public class AdminStatsDTO {
    private List<WeekData> weekData; // data for chart make a chart using chart.js

    private int totalItems;

    private int totalOrders;
    private int totalOrdersToday;//highlight

    private int deliveredOrders;
    private int notDeliveredOrders;

    private int packedOrders;
    private int notPackedOrders;

    private float totalSales;
    private float totalSalesToday;//highlight

    private int totalItemsOrdered;
    private int packedItems;
    private int itemsToBePacked;

    public AdminStatsDTO() {
    }

    public AdminStatsDTO(List<WeekData> weekData, int totalItems, int totalOrders, int totalOrdersToday, int deliveredOrders, int notDeliveredOrders, int packedOrders, int notPackedOrders, float totalSales, float totalSalesToday, int totalItemsOrdered, int packedItems, int itemsToBePacked) {
        this.weekData = weekData;
        this.totalItems = totalItems;
        this.totalOrders = totalOrders;
        this.totalOrdersToday = totalOrdersToday;
        this.deliveredOrders = deliveredOrders;
        this.notDeliveredOrders = notDeliveredOrders;
        this.packedOrders = packedOrders;
        this.notPackedOrders = notPackedOrders;
        this.totalSales = totalSales;
        this.totalSalesToday = totalSalesToday;
        this.totalItemsOrdered = totalItemsOrdered;
        this.packedItems = packedItems;
        this.itemsToBePacked = itemsToBePacked;
    }

    public List<WeekData> getWeekData() {
        return weekData;
    }

    public void setWeekData(List<WeekData> weekData) {
        this.weekData = weekData;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalOrdersToday() {
        return totalOrdersToday;
    }

    public void setTotalOrdersToday(int totalOrdersToday) {
        this.totalOrdersToday = totalOrdersToday;
    }

    public int getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(int deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public int getNotDeliveredOrders() {
        return notDeliveredOrders;
    }

    public void setNotDeliveredOrders(int notDeliveredOrders) {
        this.notDeliveredOrders = notDeliveredOrders;
    }

    public int getPackedOrders() {
        return packedOrders;
    }

    public void setPackedOrders(int packedOrders) {
        this.packedOrders = packedOrders;
    }

    public int getNotPackedOrders() {
        return notPackedOrders;
    }

    public void setNotPackedOrders(int notPackedOrders) {
        this.notPackedOrders = notPackedOrders;
    }

    public float getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(float totalSales) {
        this.totalSales = totalSales;
    }

    public float getTotalSalesToday() {
        return totalSalesToday;
    }

    public void setTotalSalesToday(float totalSalesToday) {
        this.totalSalesToday = totalSalesToday;
    }

    public int getTotalItemsOrdered() {
        return totalItemsOrdered;
    }

    public void setTotalItemsOrdered(int totalItemsOrdered) {
        this.totalItemsOrdered = totalItemsOrdered;
    }

    public int getPackedItems() {
        return packedItems;
    }

    public void setPackedItems(int packedItems) {
        this.packedItems = packedItems;
    }

    public int getItemsToBePacked() {
        return itemsToBePacked;
    }

    public void setItemsToBePacked(int itemsToBePacked) {
        this.itemsToBePacked = itemsToBePacked;
    }
}
