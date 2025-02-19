package com.elitekaycy.interfaces;

public interface EntityTransaction {
    void begin();

    void commit();

    void rollback();

    boolean isActive();
}
