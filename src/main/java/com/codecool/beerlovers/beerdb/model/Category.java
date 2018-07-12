package com.codecool.beerlovers.beerdb.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories")
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Size(max = 250)
    @Column(name = "cat_name")
    String name;

    public Category() {
    }

    public Category(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("beersJPA");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Category category = entityManager.find(Category.class, id);

        this.id = category.id;
        this.name = category.name;

        entityManager.getTransaction().commit();

    }
}
