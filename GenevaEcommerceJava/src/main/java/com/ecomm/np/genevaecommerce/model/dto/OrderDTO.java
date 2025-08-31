package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.OrderDetails;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
   private String deliveryAddress;
   private String city;
   private String province;
   private String phoneNumber;
   private int orderId;
   private String orderDate;
   private BigDecimal totalPrice;
   private BigDecimal paidPrice;
   private int noOfItems;
   private boolean isActive; //active denotes that the order is not delivered yet or cancelled.
   private boolean isProcessed; // this means packed or not
   private List<DisplayItemsDTO> imageDisplayList;
;

   public OrderDTO() {
   }

   public OrderDTO(String deliveryAddress, String city, String province, String phoneNumber, int orderId, String orderDate, BigDecimal totalPrice, BigDecimal paidPrice, int noOfItems, List<DisplayItemsDTO> imageDisplayList, boolean isActive, boolean isProcessed) {
      this.deliveryAddress = deliveryAddress;
      this.city = city;
      this.province = province;
      this.phoneNumber = phoneNumber;
      this.orderId = orderId;
      this.orderDate = orderDate;
      this.totalPrice = totalPrice;
      this.paidPrice = paidPrice;
      this.noOfItems = noOfItems;
      this.imageDisplayList = imageDisplayList;
      this.isActive = isActive;
      this.isProcessed = isProcessed;
   }



   public int getOrderId() {
      return orderId;
   }

   public void setOrderId(int orderId) {
      this.orderId = orderId;
   }

   public String getOrderDate() {
      return orderDate;
   }

   public void setOrderDate(String orderDate) {
      this.orderDate = orderDate;
   }

   public BigDecimal getTotalPrice() {
      return totalPrice;
   }

   public void setTotalPrice(BigDecimal totalPrice) {
      this.totalPrice = totalPrice;
   }

   public BigDecimal getPaidPrice() {
      return paidPrice;
   }

   public void setPaidPrice(BigDecimal paidPrice) {
      this.paidPrice = paidPrice;
   }

   public int getNoOfItems() {
      return noOfItems;
   }

   public void setNoOfItems(int noOfItems) {
      this.noOfItems = noOfItems;
   }

   public List<DisplayItemsDTO> getImageDisplayList() {
      return imageDisplayList;
   }

   public void setImageDisplayList(List<DisplayItemsDTO> imageDisplayList) {
      this.imageDisplayList = imageDisplayList;
   }

   public boolean isActive() {
      return isActive;
   }

   public void setActive(boolean active) {
      isActive = active;
   }

   public boolean isProcessed() {
      return isProcessed;
   }

   public void setProcessed(boolean processed) {
      isProcessed = processed;
   }

   public String getDeliveryAddress() {
      return deliveryAddress;
   }

   public void setDeliveryAddress(String deliveryAddress) {
      this.deliveryAddress = deliveryAddress;
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

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public static OrderDTO buildFromOrderItems(OrderedItems items){
      OrderDTO orderDataDTO = new OrderDTO();
      OrderDetails od = items.getOrderDetails();
      orderDataDTO.setDeliveryAddress(od.getDeliveryLocation());
      orderDataDTO.setPhoneNumber(od.getPhoneNumber());
      orderDataDTO.setCity(od.getCity());
      orderDataDTO.setProvince(od.getProvince());
      orderDataDTO.setActive(items.isMainActive());
      orderDataDTO.setOrderId(items.getoId());
      orderDataDTO.setTotalPrice(items.getTotalPrice());
      orderDataDTO.setPaidPrice(items.getPaidPrice());
      orderDataDTO.setProcessed(items.isProcessed());
      orderDataDTO.setNoOfItems(items.getOrderItemAuditList().size());
      orderDataDTO.setImageDisplayList(items.getOrderItemAuditList().stream().map(DisplayItemsDTO::buildFromOrderAudit).toList());
      return orderDataDTO;
   }
}
