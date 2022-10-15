package com.todo;

import com.todo.controllers.ConfigurationController;

public class DatabaseManager {
    private Database database;

    public DatabaseManager(ConfigurationController configurationController,
            IResourceHandler resourceHandler) {
        this.database =
                new Database(configurationController.getDatabaseConfiguration(), resourceHandler);
    }

    public Database getDatabase() {
        if (database.alreadyExists()) {
            return this.database;
        }

        this.database.createTables();
        return this.database;
    }
}
