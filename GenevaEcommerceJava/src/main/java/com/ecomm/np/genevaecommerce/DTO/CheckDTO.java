package com.ecomm.np.genevaecommerce.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CheckDTO {
    private String deliveryLocation;
    private String city;
    private String province;
    private String phoneNumber;
    private List<DisplayItemsDTO> displayItemsDTOList;
    private BigDecimal totalOrderPrice;


    public CheckDTO() {
    }

    public CheckDTO(String deliveryLocation, String city, String province, String phoneNumber, List<DisplayItemsDTO> displayItemsDTOList, BigDecimal totalOrderPrice) {
        this.deliveryLocation = deliveryLocation;
        this.city = city;
        this.province = province;
        this.phoneNumber = phoneNumber;
        this.displayItemsDTOList = displayItemsDTOList;
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
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

    public List<DisplayItemsDTO> getDisplayItemsDTOList() {
        return displayItemsDTOList;
    }

    public void setDisplayItemsDTOList(List<DisplayItemsDTO> displayItemsDTOList) {
        this.displayItemsDTOList = displayItemsDTOList;
    }

    public BigDecimal getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(BigDecimal totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    public BigDecimal findTotalPrice(){

        BigDecimal total = displayItemsDTOList.stream()
                .map(item -> BigDecimal.valueOf(item.getTotalItemPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalOrderPrice = total.setScale(2, RoundingMode.CEILING);

        return this.totalOrderPrice;
    }

    @Override
    public String toString() {
        return "CheckDTO{" +
                "deliveryLocation='" + deliveryLocation + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", displayItemsDTOList=" + displayItemsDTOList.toString() +
                ", totalOrderPrice=" + totalOrderPrice +
                '}';
    }
}
