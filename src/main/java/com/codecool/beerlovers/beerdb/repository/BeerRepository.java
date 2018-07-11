package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Beer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BeerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Beer getById(int id) {
        return sessionFactory
                .getCurrentSession()
                .find(Beer.class, id);
    }

    public void update(Beer beer) {

        sessionFactory.getCurrentSession().merge(beer);

    }

    public List<Beer> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT b FROM Beer b", Beer.class)
                .getResultList();
    }

    public void deleteAll() {
        sessionFactory
                .getCurrentSession()
                .createQuery("DELETE FROM Beer b")
                .executeUpdate();
    }

    public void deleteById(int id) {
        sessionFactory
                .getCurrentSession()
                .createQuery("DELETE FROM Beer b WHERE b.id = " + id)
                .executeUpdate();
    }
}
