package com.elitekaycy.utils;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Set;

import com.elitekaycy.annotations.Column;
import com.elitekaycy.annotations.GeneratedValue;
import com.elitekaycy.annotations.Id;
import com.elitekaycy.annotations.Table;
import com.elitekaycy.config.DbProperties;

public class EntityGenerator {

    public static void generateTables(Set<Class<?>> entityClasses) {
        try (Connection connection = DriverManager.getConnection(DbProperties.getJdbcUrl(), DbProperties.getUser(),
                DbProperties.getPassword())) {
            for (Class<?> entityClass : entityClasses) {
                String createTableSQL = generateCreateTableSQL(entityClass);
                System.out.println("Executing: " + createTableSQL);
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(createTableSQL);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating tables", e);
        }
    }

    private static String generateCreateTableSQL(Class<?> entityClass) {
        String tableName = getTableName(entityClass);
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Transient.class))
                continue;

            sql.append(getColumnDefinition(field)).append(", ");
        }

        sql.setLength(sql.length() - 2);
        sql.append(");");

        return sql.toString();
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.isAnnotationPresent(Table.class)
                ? entityClass.getAnnotation(Table.class).name()
                : entityClass.getSimpleName().toLowerCase();
    }

    private static String getColumnDefinition(Field field) {
        String columnName = getColumnName(field);
        String columnType = getSQLType(field.getType());
        String constraints = getConstraints(field);

        return columnName + " " + columnType + constraints;
    }

    private static String getColumnName(Field field) {
        return field.isAnnotationPresent(Column.class)
                ? field.getAnnotation(Column.class).name()
                : field.getName();
    }

    private static String getSQLType(Class<?> type) {
        if (type == int.class || type == Integer.class)
            return "INT";
        if (type == long.class || type == Long.class)
            return "BIGINT";
        if (type == String.class)
            return "VARCHAR(255)";
        if (type == double.class || type == Double.class)
            return "DOUBLE";
        if (type == boolean.class || type == Boolean.class)
            return "BOOLEAN";
        if (type == java.util.Date.class)
            return "TIMESTAMP";
        return "TEXT";
    }

    private static String getConstraints(Field field) {
        StringBuilder constraints = new StringBuilder();

        if (field.isAnnotationPresent(Id.class)) {
            constraints.append(" PRIMARY KEY");
            if (field.isAnnotationPresent(GeneratedValue.class)) {
                constraints.append(" AUTO_INCREMENT");
            }
        }

        if (field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).nullable()) {
            constraints.append(" NOT NULL");
        }

        return constraints.toString();
    }

}
