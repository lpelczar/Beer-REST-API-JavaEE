package com.codecool.beerlovers.beerdb.repository;

import com.codecool.beerlovers.beerdb.model.Category;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CategoryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Category> getAll() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("SELECT c FROM Category c", Category.class)
                .getResultList();
    }

    @Override
    public int create(Category category) {
        return (int) sessionFactory.getCurrentSession().save(category);
    }

    @Override
    public void update(Category category) {
        sessionFactory.getCurrentSession().merge(category);
    }

    @Override
    public Category getById(int id) {
        return sessionFactory.getCurrentSession().find(Category.class, id);
    }

    @Override
    public void delete(Category category) {
        sessionFactory.getCurrentSession().remove(category);
    }

    @Override
    public void deleteAll() {
        Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Category");
        query.executeUpdate();
    }
}
