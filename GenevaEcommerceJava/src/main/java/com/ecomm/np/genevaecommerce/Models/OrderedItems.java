package com.ecomm.np.genevaecommerce.Models;

import jakarta.persistence.*;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.List;

@Entity
public class OrderedItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oId;

    @ManyToMany
    @JoinTable(name = "order_items",
    joinColumns = @JoinColumn(name = "o_id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "item_code") )
    private List<Items> itemsList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    public OrderedItems() {
    }

    public OrderedItems(int oId, List<Items> itemsList, OrderDetails orderDetails) {
        this.oId = oId;
        this.itemsList = itemsList;
        this.orderDetails = orderDetails;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public List<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
