package com.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.todo.models.TodoModel;
import com.todo.models.User;

public class Database {
    private String cachedDatabaseUrl = null;
    private DatabaseConfiguration configuration;
    private IFileHandler fileHandler;

    static class RecordNotFoundException extends Exception {};

    Database(DatabaseConfiguration configuration, FileHandler fileHandler) {
        this.configuration = configuration;
        this.fileHandler = fileHandler;
    }

    public boolean alreadyExists() {
        return fileHandler.exists(this.getDatabaseUrl());
    }

    public void createTables() {
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

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + this.getDatabaseUrl());
    }

    private String getDatabaseUrl() {
        if (cachedDatabaseUrl != null) {
            return cachedDatabaseUrl;
        }

        cachedDatabaseUrl = configuration.databaseLocation + configuration.databaseFileName;;
        return cachedDatabaseUrl;
    }
}
