package com.findme.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class Dao<T> {

    private final Class<T> tClass;

    @PersistenceContext
    protected EntityManager em;

    public Dao(Class<T> tClass) {
        this.tClass = tClass;
    }

    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    public T findById(long id) {
        return em.find(this.tClass, id);
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    public void delete(T entity) {
        em.remove(em.merge(entity));
    }
}
