package com.elitekaycy.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.elitekaycy.config.DbProperties;
import com.elitekaycy.interfaces.EntityManager;
import com.elitekaycy.interfaces.EntityManagerFactory;

public class SessionManagerFactory implements EntityManagerFactory {
    private final Connection connection;

    static {
        try {
            org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console started on port 8082");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SessionManagerFactory() {
        try {
            this.connection = DriverManager.getConnection(DbProperties.getJdbcUrl(), DbProperties.getUser(),
                    DbProperties.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database connection", e);
        }
    }

    @Override
    public EntityManager createEntityManager() {
        return new SessionManager(connection);
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection", e);
        }
    }
}
