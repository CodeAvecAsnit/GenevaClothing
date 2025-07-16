package com.ecomm.np.genevaecommerce.DTO;

public class UpdateAdressDTO {
    private String address;
    private int userId;

    public UpdateAdressDTO() {
    }

    public UpdateAdressDTO(String address, int userId) {
        this.address = address;
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
