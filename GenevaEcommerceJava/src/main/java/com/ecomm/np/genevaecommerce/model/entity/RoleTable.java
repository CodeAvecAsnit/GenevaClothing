package com.ecomm.np.genevaecommerce.model.entity;


import com.ecomm.np.genevaecommerce.model.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class RoleTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "role_id")
    private int role_Id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Role role;


    @OneToMany(mappedBy = "roleTable" ,fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<UserModel> users;

    public RoleTable(int role_Id, Set<UserModel> users, Role role) {
        this.role_Id = role_Id;
        this.users = users;
        this.role = role;
    }

    public RoleTable() {
    }

    public int getRole_Id() {
        return role_Id;
    }

    public void setRole_Id(int role_Id) {
        this.role_Id = role_Id;
    }

    public Set<UserModel> getUsers() {
        return users;
    }

    public void setUsers(Set<UserModel> users) {
        this.users = users;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
