package com.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.todo.models.TodoModel;
import com.todo.models.User;

public class Database {
    private static final String databaseFileName = "todo.db";
    private static String cachedDatabaseUrl = null;

    public static IConfigurationController configurationController = new ConfigurationController();
    public static IFileHandler fileHandler = new FileHandler();

    public static class RecordNotFoundException extends Exception {};

    public Database() {}

    public static boolean alreadyExists() {
        return fileHandler.exists(getDatabaseUrl());
    }

    public static void createTables() {
        System.out.println("Creating database...");
        try {
            Connection conn = connect();
            User.createTable(conn);
            TodoModel.createTable(conn);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + getDatabaseUrl());
    }

    private static String getDatabaseUrl() {
        if (cachedDatabaseUrl != null) {
            return cachedDatabaseUrl;
        }

        configurationController.load();
        cachedDatabaseUrl = configurationController.getDatabaseLocation() + databaseFileName;
        return cachedDatabaseUrl;
    }
}
