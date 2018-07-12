package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Brewery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BreweryRepositoryImpl implements BreweryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BreweryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Brewery> getAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT b FROM Brewery b", Brewery.class).getResultList();
    }
}
