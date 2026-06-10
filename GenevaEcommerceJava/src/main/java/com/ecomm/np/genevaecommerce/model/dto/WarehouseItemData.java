package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;

public class WarehouseItemData {
    private int orderTracerCode;
    private String itemName;
    private Integer itemCode;
    private String size;
    private int quantity;

    public WarehouseItemData(int orderTracerCode, String itemName,Integer itemCode, String size, int quantity) {
        this.orderTracerCode = orderTracerCode;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.size = size;
        this.quantity = quantity;
    }

    public WarehouseItemData() {
    }

    public int getOrderTracerCode() {
        return orderTracerCode;
    }

    public void setOrderTracerCode(int orderTracerCode) {
        this.orderTracerCode = orderTracerCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemCode() {
        return itemCode;
    }

    public void setItemCode(Integer itemCode) {
        this.itemCode = itemCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static WarehouseItemData buildFromOrderItemAudit(OrderItemAudit orderItemAudit){
        WarehouseItemData wid = new WarehouseItemData();
        wid.setOrderTracerCode(orderItemAudit.getOrderTracerCode());
        wid.setItemName(orderItemAudit.getItemName());
        wid.setItemCode(orderItemAudit.getItemCode());
        wid.setSize(orderItemAudit.getSize());
        wid.setQuantity(orderItemAudit.getQuantity());
        return wid;
    }

}
