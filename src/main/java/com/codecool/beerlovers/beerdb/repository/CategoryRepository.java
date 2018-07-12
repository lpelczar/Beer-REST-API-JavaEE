package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> getAll();
    int create(Category category);
    void update(Category category);
    Category getById(int id);
    void delete(Category category);
    void deleteAll();
}
