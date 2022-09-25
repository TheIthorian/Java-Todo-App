package com.todo;

import java.sql.SQLException;

public class TodoService implements ITodoService {
    public TodoModel addTodo(String title, String description, User user) {
        TodoModel todo = new TodoModel(title, description, user);
        try {
            todo.insert(user.getId());
        } catch (SQLException e) {
            System.out.println("Unable to add todo item: " + e.getMessage());
            e.printStackTrace();
        }
        return todo;
    }

    public TodoModel updateTodo(String title, String newTitle, String newDescription, User user) {
        TodoModel todo = null;
        int userId = user.getId();

        try {
            todo = TodoModel.getByTitle(title, userId);
            todo.title = newTitle == null ? todo.title : newTitle;
            todo.description = newDescription == null ? todo.description : newDescription;
            todo.update(userId);
        } catch (SQLException e) {
            System.out.println("Unable to add todo item: " + e.getMessage());
            e.printStackTrace();
        } catch (Database.RecordNotFoundException e) {
            System.out.println("Unable to find todo item to update");
        }

        return todo;
    }

    public void deleteTodo(String title, User user) {
        System.out.println("delete: " + title);
    }

}
