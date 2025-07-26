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

    private String city;

    private String province;

//    private String country; Add only if international currently we are based in Nepal so i dont think this field is important.

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToMany(mappedBy = "orderDetails")
    private List<OrderedItems> orderedItems;


    public OrderDetails(long orderId, String deliveryLocation, String city, String province, UserModel user, List<OrderedItems> orderedItems) {
        this.orderId = orderId;
        this.deliveryLocation = deliveryLocation;
        this.city = city;
        this.province = province;
        this.user = user;
        this.orderedItems = orderedItems;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }



    public List<OrderedItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItems> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
