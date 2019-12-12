package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * instance van een eenmalige connectie met de database
 */

public class DBHelper {
    private Connection connection;
    private static final DBHelper DB_HELPER = new DBHelper();

    private DBHelper() {
        connection = connect();
    }

    public static DBHelper getInstance() {
        return DB_HELPER;
    }

    public Connection getConnection() {
        return connection;
    }

    private Connection connect() {
        String url = "jdbc:mysql://meru.hhs.nl:3306/";
        String db = "18066690";
        String user = "18066690";
        String pass = "ie4eethieK";
        try {
            connection = DriverManager.getConnection(url + db, user, pass);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
