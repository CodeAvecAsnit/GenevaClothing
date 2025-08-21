package com.ecomm.np.genevaecommerce.Security;

import com.ecomm.np.genevaecommerce.Models.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUser implements UserDetails {

    private int userId;
    private String email;
    private String password;
    private Collection<?extends GrantedAuthority> authorities;

    public CustomUser() {
    }

    public CustomUser(int userId, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    public static CustomUser build(UserModel user) {
        String roleName = user.getRoleTable().getRole().name();
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        return new CustomUser(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
        );
    }
}
