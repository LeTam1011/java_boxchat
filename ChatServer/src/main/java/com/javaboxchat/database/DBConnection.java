package com.javaboxchat.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/javaboxchat";

    private static final String USER =
            "letam1011";

    private static final String PASSWORD =
            "1234";

    public static Connection getConnection()
            throws Exception {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}
