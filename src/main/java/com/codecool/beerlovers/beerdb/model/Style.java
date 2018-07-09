package com.codecool.beerlovers.beerdb.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "styles")
@Setter
@Getter
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "cat_id")
    private Category category;

    @Column(name = "style_name")
    private String name;

    public Style() {
    }
}
