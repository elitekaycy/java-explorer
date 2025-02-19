package com.elitekaycy.interfaces;

import java.util.List;

public interface TypedQuery<T> {
    List<T> getResultList();

    T getSingleResult();

    void setParameter(String name, Object value);
}
