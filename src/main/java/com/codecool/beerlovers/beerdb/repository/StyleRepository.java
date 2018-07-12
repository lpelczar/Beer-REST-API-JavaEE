package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Style;

import java.util.List;

public interface StyleRepository {

    List<Style> getAll();
    int create(Style style);
    void update(Style style);
    Style getById(int id);
    void delete(Style style);
    void deleteAll();
}
