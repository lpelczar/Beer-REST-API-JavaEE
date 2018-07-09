package com.codecool.beerlovers.beerdb.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "breweries")
@Setter
@Getter
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 255)
    String name;

    @Size(max = 255)
    String address1;

    @Size(max = 255)
    String city;

    @Size(max = 255)
    String state;

    @Size(max = 25)
    String code;

    @Size(max = 255)
    String country;

    @Size(max = 50)
    String phone;

    @Size(max = 255)
    String website;

    String descript;

    public Brewery() {

    }

}
