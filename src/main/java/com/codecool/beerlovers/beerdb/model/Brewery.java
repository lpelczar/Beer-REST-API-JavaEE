package com.codecool.beerlovers.beerdb.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "breweries")
@Setter
@Getter
@ToString
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

    @OneToMany(mappedBy = "brewery", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Beer> beers;

    public Brewery() {

    }

    public Brewery(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("beersJPA");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Brewery brewery = entityManager.find(Brewery.class, id);

        this.id = brewery.id;
        this.name = brewery.name;
        this.address1 = brewery.address1;
        this.city = brewery.city;
        this.state = brewery.state;
        this.code = brewery.code;
        this.country = brewery.country;
        this.phone = brewery.phone;
        this.website = brewery.website;
        this.descript = brewery.descript;


        entityManager.getTransaction().commit();

    }

}
