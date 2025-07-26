package com.ecomm.np.genevaecommerce.DTO;

import com.ecomm.np.genevaecommerce.Models.OrderDetails;

public class AddressDTO {
    private String streetName;
    private String city;
    private String province;

    public AddressDTO(){}

    public AddressDTO(String streetName, String city, String province) {
        this.streetName = streetName;
        this.city = city;
        this.province = province;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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

    public static AddressDTO buildFromModel(OrderDetails orderDetails){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(orderDetails.getCity());
        addressDTO.setProvince(orderDetails.getProvince());
        addressDTO.setStreetName(orderDetails.getDeliveryLocation());
        return addressDTO;
    }
}
