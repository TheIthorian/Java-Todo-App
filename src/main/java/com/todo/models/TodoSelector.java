package com.todo.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TodoSelector {

    private Connection conn;
    private User user;

    public static class TodoDto {
        protected int todoId;
        public String title;
        public String description;
        protected int userId;

        TodoDto() {}

        TodoDto(ResultSet result) throws SQLException {
            this.todoId = result.getInt("todoId");
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
        }
    }

    public TodoSelector(Connection conn, User user) {
        this.user = user;
        this.conn = conn;
    }

    List<TodoDto> selectByTitle(String title) {
        String query = "SELECT todoId, title, description FROM todo WHERE title = ? AND userId = ?";
        List<TodoDto> output = new ArrayList<TodoDto>();

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, title);
            statement.setInt(2, user.userId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                output.add(new TodoDto(result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

    TodoDto insert(TodoDto todo) {
        String query = "INSERT INTO todo (title, description, userId) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement =
                    conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, todo.title);
            statement.setString(2, todo.description);
            statement.setInt(3, user.userId);
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                int todoId = result.getInt(1);
                return new TodoDto(todoId, todo.title, todo.description, user.userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }

    void update(TodoDto todo) {
        String query = "UPDATE todo SET title = ?, description = ? WHERE todoId = ? AND userId = ?";

        try {
            PreparedStatement statement =
                    conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, todo.title);
            statement.setString(2, todo.description);
            statement.setInt(3, todo.todoId);
            statement.setInt(4, user.userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
