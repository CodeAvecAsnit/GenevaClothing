package com.ecomm.np.genevaecommerce.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderInitiatedDate;

    @UpdateTimestamp
    private LocalDateTime orderUpdatedDate;

    private  boolean mainActive;

    private boolean processed;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;


    @Column(precision = 10, scale = 2)
    private BigDecimal paidPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "orderedItems", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemAudit> orderItemAuditList = new ArrayList<>();


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    public OrderedItems() {
    }

    public OrderedItems(int oId, LocalDateTime orderInitiatedDate, LocalDateTime orderUpdatedDate, boolean mainActive, boolean processed, BigDecimal totalPrice, BigDecimal paidPrice, List<OrderItemAudit> orderItemAuditList, OrderDetails orderDetails) {
        this.oId = oId;
        this.orderInitiatedDate = orderInitiatedDate;
        this.orderUpdatedDate = orderUpdatedDate;
        this.mainActive = mainActive;
        this.processed = processed;
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
        return mainActive;
    }

    public void setMainActive(boolean active) {
        mainActive = active;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
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
        processed = x;
        return processed;
    }

    public boolean findActive(){
        boolean x = false;
        for(OrderItemAudit oa : this.orderItemAuditList){
            x= x || oa.isActive();
        }
        mainActive = x;
        return mainActive;
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
        return mainActive;
    }

    public BigDecimal getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(BigDecimal paidPrice) {
        this.paidPrice = paidPrice;
    }
}
