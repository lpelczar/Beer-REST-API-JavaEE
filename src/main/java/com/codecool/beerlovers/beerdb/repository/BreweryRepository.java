package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Brewery;

import java.util.List;

public interface BreweryRepository {

    List<Brewery> getAll();
}
