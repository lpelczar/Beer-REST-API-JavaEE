package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Beer;
import org.springframework.stereotype.Component;

import java.util.List;

public interface BeerRepository {
    List<Beer> getAll();
    int create(Beer beer);
    Beer getById(int id);
    void update(Beer beer);
    void deleteAll();
    void deleteById(int id);
}
