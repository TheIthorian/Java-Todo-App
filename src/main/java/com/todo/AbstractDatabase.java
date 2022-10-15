package com.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.todo.models.TodoModel;
import com.todo.models.User;

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

    public void dml(String sql, Connection conn) throws SQLException {
        System.out.println(sql);
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
        statement.close();
    }

    public ResultSet select(String sql, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql);
        return statement.executeQuery();
    }

    public abstract Connection connect() throws SQLException;

}
