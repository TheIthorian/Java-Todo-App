package com.todo.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to interact with the todo database records.
 */
public class TodoSelector extends BaseSelector {

    private User user;

    /**
     * A todo dto object to mirror the database structure.
     */
    public static class TodoDto {
        protected int todoId;
        public String title;
        public String description;
        public int userId;

        public TodoDto() {}

        TodoDto(ResultSet result) throws SQLException {
            this.todoId = result.getInt("id");
            this.title = result.getString("title");
            this.description = result.getString("description");
        }

        TodoDto(int todoId, String title, String description, int userId) {
            this.todoId = todoId;
            this.title = title;
            this.description = description;
            this.userId = userId;
        }

        TodoDto(TodoDto todo) {
            this.todoId = todo.todoId;
            this.title = todo.title;
            this.description = todo.description;
            this.userId = todo.userId;
        }

        public int getId() {
            return this.todoId;
        }
    }

    public TodoSelector(User user) {
        this.user = user;
    }

    /**
     * Returns a list of all the todo items for the user.
     */
    public List<TodoDto> selectAll() throws SQLException {
        String query = "SELECT id, title, description FROM todo WHERE userId = ?";
        List<TodoDto> output = new ArrayList<TodoDto>();

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, user.userId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            output.add(new TodoDto(result));
        }

        return output;
    }

    /**
     * Returns a list of todo items which match the input title.
     */
    public List<TodoDto> selectByTitle(String title) throws SQLException {
        String query = "SELECT id, title, description FROM todo WHERE title = ? AND userId = ?";
        List<TodoDto> output = new ArrayList<TodoDto>();

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, title);
        statement.setInt(2, user.userId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            output.add(new TodoDto(result));
        }

        return output;
    }

    /**
     * Inserts a todo item.
     */
    public TodoDto insert(TodoDto todo) throws SQLException {
        String query = "INSERT INTO todo (title, description, userId) VALUES (?, ?, ?)";

        PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, todo.title);
        statement.setString(2, todo.description);
        statement.setInt(3, user.userId);
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();

        if (result.next()) {
            int todoId = result.getInt(1);
            return new TodoDto(todoId, todo.title, todo.description, user.userId);
        }

        return todo;
    }

    /**
     * Replaces the data of the input todo item.
     */
    public void update(TodoDto todo) throws SQLException {
        String query = "UPDATE todo SET title = ?, description = ? WHERE id = ? AND userId = ?";

        PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, todo.title);
        statement.setString(2, todo.description);
        statement.setInt(3, todo.todoId);
        statement.setInt(4, user.userId);
        statement.executeUpdate();
    }
}
