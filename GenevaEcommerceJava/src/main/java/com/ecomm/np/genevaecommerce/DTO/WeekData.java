package com.ecomm.np.genevaecommerce.dto;

public class WeekData {
    private String day;
    private int totalOrders;

    public WeekData(String day, int totalOrders) {
        this.day = day;
        this.totalOrders = totalOrders;
    }

    public WeekData() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
}
