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
        String serverName = "localhost:3306";
        String db = "fullhouse";
        String user = "root";
        String pass = "root";

        String databaseUrl = "jdbc:mysql://" + serverName + "/" + db + "?autoReconnect=true&useSSL=false";
        try {
            connection = DriverManager.getConnection(databaseUrl, user, pass);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
