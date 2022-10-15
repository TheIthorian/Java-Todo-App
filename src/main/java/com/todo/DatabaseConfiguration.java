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
}
