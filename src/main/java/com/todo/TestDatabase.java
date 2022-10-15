package com.todo;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to interface with the SQLite database used for automated tests.
 */
public class TestDatabase extends AbstractDatabase {

    private String dbName;

    public TestDatabase(String dbName) {
        this.dbName = dbName;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:./assets/" + dbName);
    }

    public void destroy() {
        File database = new File("./assets/" + dbName);
        database.delete();
    }

    public void refresh() {
        this.destroy();
        this.createTables();
    }
}
