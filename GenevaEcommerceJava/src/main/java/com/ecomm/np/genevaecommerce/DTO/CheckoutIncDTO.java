package com.ecomm.np.genevaecommerce.DTO;

import java.util.List;

public class CheckoutIncDTO {
    private String deliveryLocation;
    private String city;
    private String province;
    private String phoneNumber;
    private List<ItemQuantity> quantities;

    public CheckoutIncDTO(String deliveryLocation, String city, String province, String phoneNumber, List<ItemQuantity> quantities) {
        this.deliveryLocation = deliveryLocation;
        this.city = city;
        this.province = province;
        this.phoneNumber = phoneNumber;
        this.quantities = quantities;
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

    public List<ItemQuantity> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<ItemQuantity> quantities) {
        this.quantities = quantities;
    }
}
