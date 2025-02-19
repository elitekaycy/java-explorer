package com.elitekaycy.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.elitekaycy.annotations.Id;
import com.elitekaycy.annotations.Table;
import com.elitekaycy.interfaces.EntityManager;
import com.elitekaycy.interfaces.EntityTransaction;

public class SessionManager implements EntityManager {
    private Connection connection;
    private SessionTransaction transaction;

    public SessionManager(Connection connection) {
        this.connection = connection;
        this.transaction = new SessionTransaction(connection);
    }

    @Override
    public void persist(Object entity) {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);

        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder("VALUES (");

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }

            field.setAccessible(true);
            sql.append(field.getName()).append(", ");
            values.append("?, ");
        }

        sql.setLength(sql.length() - 2);
        values.setLength(values.length() - 2);
        sql.append(") ").append(values).append(");");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }
                stmt.setObject(index++, field.get(entity));
            }

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        field.set(entity, generatedKeys.getLong(1));
                        break;
                    }
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("Error persisting entity", e);
        }
    }

    @Override
    public <T> T find(Class<T> clazz, Object id) {
        String tableName = getTableName(clazz);
        String idColumn = null;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idColumn = field.getName();
                break;
            }
        }

        if (idColumn == null) {
            throw new IllegalArgumentException("No @Id field found in class " + clazz.getName());
        }

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                T entity = clazz.getDeclaredConstructor().newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(entity, resultSet.getObject(field.getName()));
                }
                return entity;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity", e);
        }
        return null;
    }

    @Override
    public void remove(Object entity) {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);

        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
                break;
            }
        }

        if (idField == null) {
            throw new IllegalArgumentException("No @Id field found in class " + clazz.getName());
        }

        idField.setAccessible(true);
        try {
            Object idValue = idField.get(entity);
            if (idValue == null) {
                throw new IllegalArgumentException("Cannot delete entity without an ID value");
            }

            String sql = "DELETE FROM " + tableName + " WHERE " + idField.getName() + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setObject(1, idValue);
                stmt.executeUpdate();
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("Error deleting entity", e);
        }
    }

    @Override
    public EntityTransaction getTransaction() {
        return transaction;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error closing session ", e);
        }
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.isAnnotationPresent(Table.class)
                ? entityClass.getAnnotation(Table.class).name()
                : entityClass.getSimpleName().toLowerCase();
    }

}
