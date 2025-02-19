package com.elitekaycy.interfaces;

public interface EntityManager {

    void persist(Object entity);

    <T> T find(Class<T> entityClass, Object primaryKey);

    void remove(Object entity);

    EntityTransaction getTransaction();

    void close();

}
