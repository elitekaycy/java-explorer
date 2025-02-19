package com.elitekaycy.interfaces;

public interface EntityManagerFactory {
    EntityManager createEntityManager();

    void close();
}
