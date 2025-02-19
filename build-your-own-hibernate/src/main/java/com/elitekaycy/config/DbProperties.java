package com.elitekaycy.config;

public class DbProperties {

    private static String jdbcUrl = "jdbc:h2:~/test";
    private static String user = "sa";
    private static String password = "";

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
