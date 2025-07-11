package com.ecomm.np.genevaecommerce.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "ecommerce_users")
public class UserModel {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int user_id;

    @Column
            (length = 20,unique = true,nullable = false)
    private String user_name;

    @Column (length = 50, unique = true,nullable = false)
    private String email;


    @Column(length = 60)
    private String password;


    @ManyToOne
    @JoinColumn(name ="role_id")
    private RoleTable roleTable;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;


    @OneToMany(mappedBy = "user")
    private List<OrderDetails> userOrders;

    public UserModel() {
    }

    public UserModel(String user_name, String email, String password) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public RoleTable getRoleTable() {
        return roleTable;
    }

    public void setRoleTable(RoleTable roleTable) {
        this.roleTable = roleTable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public List<OrderDetails> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(List<OrderDetails> userOrders) {
        this.userOrders = userOrders;
    }
}
