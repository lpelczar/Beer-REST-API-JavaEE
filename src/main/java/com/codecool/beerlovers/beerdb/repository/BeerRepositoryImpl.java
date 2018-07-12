package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Beer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BeerRepositoryImpl implements BeerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Beer getById(int id) {
        return sessionFactory
                .getCurrentSession()
                .find(Beer.class, id);
    }

    @Override
    public void update(Beer beer) {

        sessionFactory.getCurrentSession().merge(beer);

    }

    @Override
    public List<Beer> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT b FROM Beer b ORDER BY b.id", Beer.class)
                .getResultList();
    }

    @Override
    public List<Beer> getAll(int start, int limit) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT b FROM Beer b ORDER BY b.id ASC", Beer.class)
                .setFirstResult(start).setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void deleteAll() {
        sessionFactory
                .getCurrentSession()
                .createQuery("DELETE FROM Beer b")
                .executeUpdate();
    }

    @Override
    public void deleteById(int id) {
        sessionFactory
                .getCurrentSession()
                .createQuery("DELETE FROM Beer b WHERE b.id = " + id)
                .executeUpdate();
    }

    @Override
    public int create(Beer beer) {
        return (int) sessionFactory.getCurrentSession().save(beer);
    }
}
