package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

import java.math.BigDecimal;

public class HistoryDTO {
    private int orderId;
    private BigDecimal remainingPrice;
    private String orderDate;
    private boolean isActive;// if passive means order is devlivered show deliverd and grey color prolly
    private boolean isProcessed;//this states that order is packed or not color if packed green otherwise a bit dark yellow

    public HistoryDTO() {
    }

    public HistoryDTO(int orderId, BigDecimal remainingPrice, String orderDate, boolean isActive, boolean isProcessed) {
        this.orderId = orderId;
        this.remainingPrice = remainingPrice;
        this.orderDate = orderDate;
        this.isActive = isActive;
        this.isProcessed = isProcessed;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getRemainingPrice() {
        return remainingPrice;
    }

    public void setRemainingPrice(BigDecimal remainingPrice) {
        this.remainingPrice = remainingPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public static HistoryDTO buildFromOrderItems(OrderedItems orderedItems) {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setOrderId(orderedItems.getoId());

        // Safe BigDecimal subtraction with null checks
        BigDecimal totalPrice = orderedItems.getTotalPrice() != null ? orderedItems.getTotalPrice() : BigDecimal.ZERO;
        BigDecimal paidPrice = orderedItems.getPaidPrice() != null ? orderedItems.getPaidPrice() : BigDecimal.ZERO;

        BigDecimal remaining = totalPrice.subtract(paidPrice);
        historyDTO.setRemainingPrice(remaining);

        historyDTO.setProcessed(orderedItems.isProcessed());
        historyDTO.setActive(orderedItems.isMainActive());
        return historyDTO;
    }
}
