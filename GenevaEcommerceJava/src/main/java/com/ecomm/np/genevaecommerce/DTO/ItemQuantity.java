package com.ecomm.np.genevaecommerce.DTO;

public class ItemQuantity {
    private int itemCode;
    private int quantity;

    public ItemQuantity(int itemCode, int quantity) {
        this.itemCode = itemCode;
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
}
