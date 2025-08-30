package com.ecomm.np.genevaecommerce.dto;

public class LoginResponseDTO {
    private int responseCode;
    private String message;
    private String jwtToken;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(int responseCode, String message, String jwtToken) {
        this.responseCode = responseCode;
        this.message = message;
        this.jwtToken = jwtToken;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}
