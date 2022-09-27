package com.todo.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import com.todo.Database;
import com.todo.ITodoService;
import com.todo.models.TodoModel;
import com.todo.models.TodoSelector;
import com.todo.models.User;

public class TodoService implements ITodoService {

    private Database database;

    public TodoService(Database database) {
        this.database = database;
    }

    public void addTodo(String title, String description, User user) {
        try {
            Connection conn = database.connect();
            TodoSelector todoSelector = new TodoSelector(conn, user);
            TodoModel todo = new TodoModel(title, description, user);
            todo.insert(todoSelector);
            conn.close();
            System.out.print("Todo successfully added.");
        } catch (SQLException e) {
            System.out.print("Unable to add new todo:");
            e.printStackTrace();
        }
    }

    public void updateTodo(String title, String newTitle, String newDescription, User user) {
        try {
            Connection conn = database.connect();
            TodoSelector todoSelector = new TodoSelector(conn, user);
            TodoModel existingTodo = TodoModel.getByTitle(todoSelector, title).get(0);

            existingTodo.title = newTitle == null ? existingTodo.title : newTitle;
            existingTodo.description =
                    newDescription == null ? existingTodo.description : newDescription;

            existingTodo.update(todoSelector);
            conn.close();
            System.out.print("Todo successfully updated.");
        } catch (SQLException e) {
            System.out.print("Unable to update new todo:");
            e.printStackTrace();
        }
    }

    public void deleteTodo(String title, User user) {
        System.out.println("delete: " + title);
    }

}
