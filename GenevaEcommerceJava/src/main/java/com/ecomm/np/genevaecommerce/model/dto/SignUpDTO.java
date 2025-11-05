package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignUpDTO {
    @Email
    private String email;
    @NotBlank
    private String password;

    private String username;

    @JsonIgnore
    private int code;

    @JsonIgnore
    private int mailsSent;

    public SignUpDTO(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public SignUpDTO() {
    }

    public SignUpDTO(String email, String password, String username, int code) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.code = code;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static UserModel build(SignUpDTO signUpDTO){
        UserModel userModel = new UserModel();
        userModel.setUserName(signUpDTO.getUsername());
        userModel.setPassword(signUpDTO.getPassword());
        userModel.setEmail(signUpDTO.getEmail());
        return userModel;
    }

    public boolean canSendMail(){
        return this.mailsSent < 5;
    }

}
