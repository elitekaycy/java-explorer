package com.elitekaycy.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.elitekaycy.interfaces.EntityTransaction;

public class SessionTransaction implements EntityTransaction {
    private final Connection connection;
    private boolean active = false;

    public SessionTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() {
        try {
            if (!active) {
                connection.setAutoCommit(false);
                active = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error beginning transaction", e);
        }
    }

    @Override
    public void commit() {
        try {
            if (active) {
                connection.commit();
                connection.setAutoCommit(true);
                active = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error committing transaction", e);
        }
    }

    @Override
    public void rollback() {
        try {
            if (active) {
                connection.rollback();
                connection.setAutoCommit(true);
                active = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error rolling back transaction", e);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

}
