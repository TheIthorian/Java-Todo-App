package com.todo.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.todo.Database;
import com.todo.ITodoService;
import com.todo.models.TodoModel;
import com.todo.models.TodoSelector;
import com.todo.models.User;

public class TodoService implements ITodoService {

    private Database database;
    private User user;

    public TodoSelector selector;

    public TodoService(Database database, User user) {
        this.database = database;
        this.user = user;
        selector = new TodoSelector(user);
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TodoModel> getAll() {
        final List<TodoModel> output = new ArrayList<TodoModel>();
        try {
            selector.connect(database);
            final List<TodoSelector.TodoDto> todoItems = selector.selectAll();
            for (TodoSelector.TodoDto todoItem : todoItems) {
                output.add((TodoModel) new TodoModel(todoItem));
            }
            selector.disconnect();
        } catch (SQLException e) {
            System.out.print("Unable to get new todo items:");
            e.printStackTrace();
        }
        return output;
    }

    public List<TodoModel> getByTitle(String title) {
        final List<TodoModel> output = new ArrayList<TodoModel>();
        try {
            selector.connect(database);
            final List<TodoSelector.TodoDto> todoItems = selector.selectByTitle(title);
            for (TodoSelector.TodoDto todoItem : todoItems) {
                output.add((TodoModel) new TodoModel(todoItem));
            }
            selector.disconnect();
        } catch (SQLException e) {
            System.out.print("Unable to get new todo items:");
            e.printStackTrace();
        }
        return output;
    }

    public void addTodo(String title, String description) {
        try {
            TodoModel todo = new TodoModel(title, description, user);

            selector.connect(database);
            selector.insert(todo);
            selector.disconnect();

            System.out.print("Todo successfully added.");
        } catch (SQLException e) {
            System.out.print("Unable to add new todo:");
            e.printStackTrace();
        }
    }

    public void updateTodo(String title, String newTitle, String newDescription) {
        try {
            TodoModel existingTodo = getByTitle(title).get(0);

            existingTodo.title = newTitle == null ? existingTodo.title : newTitle;
            existingTodo.description =
                    newDescription == null ? existingTodo.description : newDescription;

            selector.connect(database);
            selector.update(existingTodo);
            selector.disconnect();

            System.out.print("Todo successfully updated.");
        } catch (SQLException e) {
            System.out.print("Unable to update new todo:");
            e.printStackTrace();
        }
    }

    public void deleteTodo(String title) {
        System.out.println("delete: " + title);
    }

}
