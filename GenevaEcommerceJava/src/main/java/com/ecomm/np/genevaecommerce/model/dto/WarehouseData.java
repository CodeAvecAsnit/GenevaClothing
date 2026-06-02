package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

import java.util.List;

public class WarehouseData {

    private int oId;
    private List<WarehouseItemData> items;

    public WarehouseData(){}

    public WarehouseData(int oId, List<WarehouseItemData> items) {
        this.oId = oId;
        this.items = items;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public List<WarehouseItemData> getItems() {
        return items;
    }

    public void setItems(List<WarehouseItemData> items) {
        this.items = items;
    }

    public static WarehouseData buildFromOrder(OrderedItems orderedItems){
        WarehouseData warehouseData = new WarehouseData();
        warehouseData.setoId(orderedItems.getoId());
        List<WarehouseItemData> wID = orderedItems.getOrderItemAuditList().
                stream().
                map(WarehouseItemData::buildFromOrderItemAudit).
                toList();
        warehouseData.setItems(wID);
        return warehouseData;
    }
}
