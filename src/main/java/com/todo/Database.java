package com.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String databaseFileName = "todo.db";
    private static String cachedDatabaseUrl = null;

    public static IConfigurationController configurationController = new ConfigurationController();

    public static class RecordNotFoundException extends Exception {};

    public Database() {}

    public static void createTables() {
        try {
            Connection conn = connect();
            User.createTable(conn);
            TodoModel.createTable(conn);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(getDatabaseUrl());
    }

    private static String getDatabaseUrl() {
        if (cachedDatabaseUrl != null) {
            return cachedDatabaseUrl;
        }

        cachedDatabaseUrl = configurationController.getDatabaseLocation() + databaseFileName;
        return cachedDatabaseUrl;
    }
}
