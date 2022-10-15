package com.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.todo.models.TodoModel;
import com.todo.models.User;

/**
 * Abstract database class with generic DDL and DML methods.
 */
public abstract class AbstractDatabase {
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

    /**
     * Executes a sql statement.
     */
    public void dml(String sql, Connection conn) throws SQLException {
        System.out.println(sql);
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Executes a sql statement and returns the resultSet.
     */
    public ResultSet select(String sql, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql);
        return statement.executeQuery();
    }

    /**
     * Connect the the database, returning a `Connection` object.
     */
    public abstract Connection connect() throws SQLException;

}
