package com.ecomm.np.genevaecommerce.dto;

public class VerificationDTO {
    private String email;
    private int code;

    public VerificationDTO() {
    }

    public VerificationDTO(String email, int code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
