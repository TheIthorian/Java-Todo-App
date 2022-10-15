package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A model of a todo item.
 */
public class TodoModel extends TodoSelector.TodoDto {
    public TodoModel(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.userId = user.getId();
    }

    public TodoModel(TodoSelector.TodoDto todo) {
        super(todo);
    }

    /**
     * Executes a DDL statement to create the `todo` table.
     */
    public static void createTable(Connection conn) throws SQLException {
        final String createTodo = "CREATE TABLE IF NOT EXISTS todo ("
                + "id integer PRIMARY KEY, title text NOT NULL, description text, userId integer REFERENCES users (userId))";

        final Statement statement = conn.createStatement();
        statement.execute(createTodo);
        statement.close();
    }
}
