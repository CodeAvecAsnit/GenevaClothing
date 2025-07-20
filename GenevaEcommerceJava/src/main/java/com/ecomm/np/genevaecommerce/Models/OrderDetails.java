package com.ecomm.np.genevaecommerce.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long orderId;

    @Lob
    private String deliveryLocation;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToMany(mappedBy = "orderDetails")
    private List<OrderedItems> orderedItems;

    public OrderDetails(long orderId, String deliveryLocation, UserModel user, List<OrderedItems> orderedItems) {
        this.orderId = orderId;
        this.deliveryLocation = deliveryLocation;
        this.user = user;
        this.orderedItems = orderedItems;
    }

    public OrderDetails() {

    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<OrderedItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItems> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
