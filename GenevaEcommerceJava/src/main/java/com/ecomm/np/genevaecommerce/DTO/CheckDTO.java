package com.ecomm.np.genevaecommerce.DTO;

import java.util.List;

public class CheckDTO {
    private String deliveryLocation;
    private String city;
    private String province;
    private String phoneNumber;
    private List<DisplayItemsDTO> displayItemsDTOList;
    private float totalOrderPrice;


    public CheckDTO() {
    }

    public CheckDTO(String deliveryLocation, String city, String province, String phoneNumber, List<DisplayItemsDTO> displayItemsDTOList, float totalOrderPrice) {
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

    public float getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(float totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public float findTotalPrice(){
        this.totalOrderPrice = displayItemsDTOList.stream().map(DisplayItemsDTO::getPrice).reduce(0f,Float::sum);
        return this.totalOrderPrice;
    }
}
