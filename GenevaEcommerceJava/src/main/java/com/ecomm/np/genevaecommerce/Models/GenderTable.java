package com.ecomm.np.genevaecommerce.Models;

import com.ecomm.np.genevaecommerce.Enumerations.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class GenderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id")
    private int genderId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "genderTable",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Items> itemList;
}
