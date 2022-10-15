package com.todo;

/**
 * DTO class to hold database configuration.
 */
public class DatabaseConfiguration {
    String databaseLocation;
    String databaseFileName;

    public DatabaseConfiguration(String databaseLocation, String databaseFileName) {
        this.databaseLocation = databaseLocation;
        this.databaseFileName = databaseFileName;
    }

    /**
     * Returns a DatabaseConfiguration object which is used to create an in-memory sqlite database.
     */
    public static DatabaseConfiguration InMemory() {
        return new DatabaseConfiguration("", ":memory:");
    }
}
