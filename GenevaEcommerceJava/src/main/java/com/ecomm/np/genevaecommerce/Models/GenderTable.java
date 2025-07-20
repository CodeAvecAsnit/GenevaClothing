package com.ecomm.np.genevaecommerce.Models;

import com.ecomm.np.genevaecommerce.Enumerations.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class GenderTable {
    @Id
    @Column(name = "gender_id")
    private int genderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @OneToMany(mappedBy = "genderTable",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Items> itemList;

    public GenderTable() {
    }

    public GenderTable(int genderId, Gender gender, List<Items> itemList) {
        this.genderId = genderId;
        this.gender = gender;
        this.itemList = itemList;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Items> getItemList() {
        return itemList;
    }

    public void setItemList(List<Items> itemList) {
        this.itemList = itemList;
    }
}
