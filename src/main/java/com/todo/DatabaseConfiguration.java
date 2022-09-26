package com.todo;

public class DatabaseConfiguration {
    String databaseLocation;
    String databaseFileName;

    DatabaseConfiguration(String databaseLocation, String databaseFileName) {
        this.databaseLocation = databaseLocation;
        this.databaseFileName = databaseFileName;
    }
}
