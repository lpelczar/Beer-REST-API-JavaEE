package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Style;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StyleRepositoryImpl implements StyleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public StyleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Style> getAll() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("SELECT s FROM Style s", Style.class)
                .getResultList();
    }

    @Override
    public int create(Style style) {
        return (int) sessionFactory.getCurrentSession().save(style);
    }

    @Override
    public void update(Style style) {
        sessionFactory.getCurrentSession().merge(style);
    }

    @Override
    public Style getById(int id) {
        return sessionFactory.getCurrentSession().find(Style.class, id);
    }

    @Override
    public void delete(Style style) {
        sessionFactory.getCurrentSession().remove(style);
    }

    @Override
    public void deleteAll() {
        Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Style");
        query.executeUpdate();
    }
}
