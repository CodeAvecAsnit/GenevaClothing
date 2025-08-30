package com.ecomm.np.genevaecommerce.dto;

public class QuantityItemDTO {
    private int itemCode;
    private String size;
    private int quantity;


    public QuantityItemDTO(){}
    public QuantityItemDTO(int itemCode, String size, int quantity) {
        this.itemCode = itemCode;
        this.size = size;
        this.quantity = quantity;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
