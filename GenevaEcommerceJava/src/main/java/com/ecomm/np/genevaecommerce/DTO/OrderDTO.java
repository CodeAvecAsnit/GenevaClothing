package com.ecomm.np.genevaecommerce.DTO;

public class OrderDTO {
    private String address;
    private int quantity;
    private int item_id;
    private int user_id;

    public OrderDTO() {
    }

    public OrderDTO(String address, int quantity, int item_id,int user_id) {
        this.address = address;
        this.quantity = quantity;
        this.item_id = item_id;
        this.user_id = user_id;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }
}
