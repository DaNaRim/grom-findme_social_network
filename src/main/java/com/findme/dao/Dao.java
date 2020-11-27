package com.findme.dao;

import com.findme.exception.InternalServerException;
import org.hibernate.HibernateException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Dao<T> {

    private final Class<T> tClass;

    public Dao(Class<T> tClass) {
        this.tClass = tClass;
    }

    @PersistenceContext
    protected EntityManager em;

    public T save(T entity) throws InternalServerException {
        try {
            em.persist(entity);

            return entity;
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to save entity " + e.getMessage());
        }
    }

    public T findById(long id) throws InternalServerException {
        try {
            return em.find(this.tClass, id);

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to find entity by id: "
                    + e.getMessage());
        }
    }

    public T update(T entity) throws InternalServerException {
        try {
            return em.merge(entity);

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to update entity: " + e.getMessage());
        }
    }

    public void delete(T entity) throws InternalServerException {
        try {
            em.remove(em.merge(entity));

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to delete entity: " + e.getMessage());
        }
    }
}
