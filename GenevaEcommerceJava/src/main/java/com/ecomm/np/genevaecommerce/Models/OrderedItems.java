package com.ecomm.np.genevaecommerce.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class OrderedItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oId;

    @UpdateTimestamp
    private LocalDateTime orderInitiatedDate;

    @UpdateTimestamp
    private LocalDateTime orderUpdatedDate;

    private  boolean isMainActive;

    private boolean isProcessed;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;


    @Column(precision = 10, scale = 2)
    private BigDecimal paidPrice;

    @JsonIgnore
    @OneToMany
    private List<OrderItemAudit> orderItemAuditList = new ArrayList<>();


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    public OrderedItems() {
    }

    public OrderedItems(int oId, LocalDateTime orderInitiatedDate, LocalDateTime orderUpdatedDate, boolean isMainActive, boolean isProcessed, BigDecimal totalPrice, BigDecimal paidPrice, List<OrderItemAudit> orderItemAuditList, OrderDetails orderDetails) {
        this.oId = oId;
        this.orderInitiatedDate = orderInitiatedDate;
        this.orderUpdatedDate = orderUpdatedDate;
        this.isMainActive = isMainActive;
        this.isProcessed = isProcessed;
        this.totalPrice = totalPrice;
        this.paidPrice = paidPrice;
        this.orderItemAuditList = orderItemAuditList;
        this.orderDetails = orderDetails;
    }

    public List<OrderItemAudit> getOrderItemAuditList() {
        return orderItemAuditList;
    }

    public void setOrderItemAuditList(List<OrderItemAudit> orderItemAuditList) {
        this.orderItemAuditList = orderItemAuditList;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }


    public boolean isActive() {
        return isMainActive;
    }

    public void setMainActive(boolean active) {
        isMainActive = active;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public LocalDateTime getOrderInitiatedDate() {
        return orderInitiatedDate;
    }

    public void setOrderInitiatedDate(LocalDateTime orderInitiatedDate) {
        this.orderInitiatedDate = orderInitiatedDate;
    }

    public LocalDateTime getOrderUpdatedDate() {
        return orderUpdatedDate;
    }

    public void setOrderUpdatedDate(LocalDateTime orderUpdatedDate) {
        this.orderUpdatedDate = orderUpdatedDate;
    }

    public boolean findProcessed(){
        boolean x = true;
        for(OrderItemAudit oa : this.orderItemAuditList){
            x=x && oa.isActive();
        }
        isProcessed = x;
        return isProcessed;
    }

    public boolean findActive(){
        boolean x = false;
        for(OrderItemAudit oa : this.orderItemAuditList){
            x= x || oa.isActive();
        }
        isMainActive = x;
        return isMainActive;
    }

    public void findTotalOrderPrice() {
        BigDecimal sum = BigDecimal.ZERO;

        for (OrderItemAudit oa : this.orderItemAuditList) {
            float itemTotal = oa.setTotalPrice();
            BigDecimal itemTotalBD = BigDecimal.valueOf(itemTotal);
            sum = sum.add(itemTotalBD);
        }
        sum = sum.setScale(2, RoundingMode.HALF_UP);
        this.totalPrice = sum;
    }

    public boolean isMainActive() {
        return isMainActive;
    }

    public BigDecimal getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(BigDecimal paidPrice) {
        this.paidPrice = paidPrice;
    }
}
