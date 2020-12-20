package com.findme.dao;

import com.findme.exception.InternalServerException;
import org.hibernate.HibernateException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class Dao<T> {

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
            throw new InternalServerException("Dao.save failed: " + e.getMessage());
        }
    }

    public T findById(long id) throws InternalServerException {
        try {
            return em.find(this.tClass, id);

        } catch (HibernateException e) {
            throw new InternalServerException("Dao.findById failed: " + e.getMessage());
        }
    }

    public T update(T entity) throws InternalServerException {
        try {
            return em.merge(entity);

        } catch (HibernateException e) {
            throw new InternalServerException("Dao.update failed: " + e.getMessage());
        }
    }

    public void delete(T entity) throws InternalServerException {
        try {
            em.remove(em.merge(entity));

        } catch (HibernateException e) {
            throw new InternalServerException("Dao.delete failed: " + e.getMessage());
        }
    }
}
