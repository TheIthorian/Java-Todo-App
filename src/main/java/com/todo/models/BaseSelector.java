package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import com.todo.Database;

public abstract class BaseSelector {
    public Connection conn;

    public void connect(Database database) throws SQLException {
        this.conn = database.connect();
    }

    public void disconnect() throws SQLException {
        this.conn.close();
        this.conn = null;
    }
}
