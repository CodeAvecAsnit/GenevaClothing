package com.ecomm.np.genevaecommerce.DTO;


import com.ecomm.np.genevaecommerce.Models.UserModel;

public class UserDTO {
    private String username;
    private String email;
    private String password;


    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    public static UserModel userModelBuild(UserDTO userDTO){
        UserModel model = new UserModel();
        model.setUserName(userDTO.getUsername());
        model.setPassword(userDTO.getPassword());
        model.setEmail(userDTO.getEmail());
        return model;
    }
}
