package com.codecool.beerlovers.beerdb.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
    @Size(max = 255)
    private String name;

    public Style() {
    }

    public Style(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("beersJPA");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Style style = entityManager.find(Style.class, id);

        this.id = style.id;
        this.name = style.name;
        this.category = style.category;

        entityManager.getTransaction().commit();

    }
}
