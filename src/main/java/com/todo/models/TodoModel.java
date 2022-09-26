package com.todo.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.todo.Database;

public class TodoModel {
    private int todoId;
    public String title;
    public String description;
    public int userId;

    public TodoModel(int todoId, String title, String description, int userId) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public TodoModel(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.userId = user.getId();
    }

    public int getId() {
        return this.todoId;
    }

    public static TodoModel getByTitle(String title, int userId)
            throws Database.RecordNotFoundException, SQLException {
        String query = "SELECT todoId, title, description FROM todo WHERE title = ? AND userId = ?";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, title);
        statement.setInt(2, userId);
        ResultSet result = statement.executeQuery();
        conn.close();

        if (result.next()) {
            return new TodoModel(result.getInt("todoId"), result.getString("description"),
                    result.getString("description"), userId);
        } else {
            throw new Database.RecordNotFoundException();
        }
    }

    public void update(int userId) throws SQLException {
        String query = "UPDATE todo SET title = ?, description = ? WHERE todoId = ? userId = ?";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, this.title);
        statement.setString(2, this.description);
        statement.setInt(3, this.todoId);
        statement.setInt(4, userId);
        statement.executeUpdate();
        conn.close();
    }

    public void insert(int userId) throws SQLException {
        String query = "INSERT INTO todo (title, description, userId) VALUES (?, ?, ?)";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.title);
        statement.setString(2, this.description);
        statement.setInt(3, userId);
        statement.executeUpdate();

        ResultSet result = statement.getGeneratedKeys();
        result.next();
        this.todoId = result.getInt(1);
        conn.close();
    }

    public static void createTable(Connection conn) throws SQLException {
        String createTodo = "CREATE TABLE IF NOT EXISTS todo ("
                + "id integer PRIMARY KEY, title text NTO NULL, description text, userId integer REFERENCES users (userId))";

        Statement statement = conn.createStatement();
        statement.execute(createTodo);
        statement.close();
    }
}
