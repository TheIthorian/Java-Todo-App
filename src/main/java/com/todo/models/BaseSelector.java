package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import com.todo.AbstractDatabase;

/**
 * Abstract class for selectors. Includes `connect` and `disconnect` methods.
 */
public abstract class BaseSelector {
    public Connection conn;

    /**
     * Connects the selector instance to the database.
     */
    public void connect(AbstractDatabase database) throws SQLException {
        this.conn = database.connect();
    }

    /**
     * Drops the current database connection.
     */
    public void disconnect() throws SQLException {
        this.conn.close();
        this.conn = null;
    }
}
