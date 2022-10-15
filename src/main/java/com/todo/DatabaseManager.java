package com.todo;

import com.todo.controllers.ConfigurationController;

/**
 * Class to control access and state of the database.
 */
public class DatabaseManager {
    private Database database;

    public DatabaseManager(ConfigurationController configurationController,
            IResourceHandler resourceHandler) {
        this.database =
                new Database(configurationController.getDatabaseConfiguration(), resourceHandler);
    }

    /**
     * Returns a `database` object.
     */
    public Database getDatabase() {
        if (database.alreadyExists()) {
            return this.database;
        }

        this.database.createTables();
        return this.database;
    }
}
