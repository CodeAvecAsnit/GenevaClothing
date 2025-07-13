package com.ecomm.np.genevaecommerce.Models;
import jakarta.persistence.*;


@Entity
public class OrderedItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oId;

    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="item_code")
    private Items item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    public OrderedItems() {
    }

    public OrderedItems(int oId, int quantity, Items item, OrderDetails orderDetails) {
        this.oId = oId;
        this.quantity = quantity;
        this.item = item;
        this.orderDetails = orderDetails;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
