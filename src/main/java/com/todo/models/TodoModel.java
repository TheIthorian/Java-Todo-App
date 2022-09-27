package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TodoModel extends TodoSelector.TodoDto {
    public TodoModel(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.userId = user.getId();
    }

    private TodoModel(TodoSelector.TodoDto todo) {
        super(todo);
    }

    public int getId() {
        return this.todoId;
    }

    public void insert(TodoSelector selector) throws SQLException {
        selector.insert(this);
    }

    public void update(TodoSelector selector) throws SQLException {
        selector.update(this);
    }

    public static List<TodoModel> getByTitle(TodoSelector selector, String title)
            throws SQLException {
        final List<TodoSelector.TodoDto> todoItems = selector.selectByTitle(title);

        final List<TodoModel> output = new ArrayList<TodoModel>();
        for (TodoSelector.TodoDto todoItem : todoItems) {
            output.add((TodoModel) new TodoModel(todoItem));
        }

        return output;
    }

    public static void createTable(Connection conn) throws SQLException {
        final String createTodo = "CREATE TABLE IF NOT EXISTS todo ("
                + "id integer PRIMARY KEY, title text NTO NULL, description text, userId integer REFERENCES users (userId))";

        final Statement statement = conn.createStatement();
        statement.execute(createTodo);
        statement.close();
        conn.close();
    }
}
