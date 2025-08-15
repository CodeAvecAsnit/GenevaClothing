package com.ecomm.np.genevaecommerce.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    private float totalPrice;

    @JsonIgnore
    @OneToMany
    private List<OrderItemAudit> orderItemAuditList;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    public OrderedItems() {
    }

    public OrderedItems(int oId, LocalDateTime orderInitiatedDate, LocalDateTime orderUpdatedDate, boolean isMainActive, boolean isProcessed, float totalPrice, List<OrderItemAudit> orderItemAuditList, OrderDetails orderDetails) {
        this.oId = oId;
        this.orderInitiatedDate = orderInitiatedDate;
        this.orderUpdatedDate = orderUpdatedDate;
        this.isMainActive = isMainActive;
        this.isProcessed = isProcessed;
        this.totalPrice = totalPrice;
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

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
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

    public float findTotalOrderPrice(){
        float sum = 0f;
        for(OrderItemAudit oa : this.orderItemAuditList){
            sum+=oa.setTotalPrice();
        }
        totalPrice = sum;
        return totalPrice;
    }
}
