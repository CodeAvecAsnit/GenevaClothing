package com.ecomm.np.genevaecommerce.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table (name = "ecommerce_users")
public class UserModel {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column
            (length = 20,unique = true,nullable = false)
    private String userName;

    @Column (length = 50, unique = true,nullable = false)
    private String email;


    @Column(length = 60)
    private String password;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="role_id")
    @JsonBackReference
    private RoleTable roleTable;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private OrderDetails userOrders;

    @ManyToMany
    @JoinTable(name ="cartlist",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "item_code")
    )
    @JsonBackReference
    private Set<Items> cartList;

    @ManyToMany
    @JoinTable(name ="wishlist",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "item_code")
    )
    @JsonBackReference
    private Set<Items> wishList;

    public UserModel() {
    }

    public UserModel(String user_name, String email, String password) {
        this.userName = user_name;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Set<Items> getWishList() {
        return wishList;
    }

    public void setWishList(Set<Items> wishList) {
        this.wishList = wishList;
    }

    public Set<Items> getCartList() {
        return cartList;
    }

    public void setCartList(Set<Items> cartList) {
        this.cartList = cartList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OrderDetails getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(OrderDetails userOrders) {
        this.userOrders = userOrders;
    }

    public void addToCart(Items item){
        cartList.add(item);
    }

    public void addToWishList(Items item){
        cartList.add(item);
    }
}
