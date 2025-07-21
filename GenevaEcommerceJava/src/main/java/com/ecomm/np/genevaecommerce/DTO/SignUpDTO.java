package com.ecomm.np.genevaecommerce.DTO;

import com.ecomm.np.genevaecommerce.Models.UserModel;

public class SignUpDTO {
    private String email;
    private String password;
    private String username;

    public SignUpDTO(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public SignUpDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static UserModel build(SignUpDTO signUpDTO){
        UserModel userModel = new UserModel();
        userModel.setUserName(signUpDTO.getUsername());
        userModel.setPassword(signUpDTO.getPassword());
        userModel.setEmail(signUpDTO.getEmail());
        return userModel;
    }
}
