package com.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to interface with the SQLite database.
 */
public class Database extends AbstractDatabase {
    private String cachedDatabaseUrl = null;
    private DatabaseConfiguration configuration;
    private IResourceHandler resourceHandler;

    public Database(DatabaseConfiguration configuration, IResourceHandler resourceHandler) {
        this.configuration = configuration;
        this.resourceHandler = resourceHandler;
    }

    /**
     * Returns true if the database has already been created.
     */
    public boolean alreadyExists() {
        return resourceHandler.exists(this.getDatabaseUrl());
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + this.getDatabaseUrl());
    }

    private String getDatabaseUrl() {
        if (cachedDatabaseUrl != null) {
            return cachedDatabaseUrl;
        }

        cachedDatabaseUrl = configuration.databaseLocation + configuration.databaseFileName;
        return cachedDatabaseUrl;
    }
}
