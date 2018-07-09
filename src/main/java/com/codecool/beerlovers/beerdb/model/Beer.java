package com.codecool.beerlovers.beerdb.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "beers")
@Setter
@Getter
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "brewery_id")
    private Brewery brewery;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "cat_id")
    private Category category;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "style_id")
    private Style style;

    @Size(max = 255)
    private String name;

    private float abv;

    private float ibu;

    private float srm;

    private int upc;

    private String descript;


    public Beer() {
    }
}
