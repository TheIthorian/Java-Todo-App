package com.todo;

import com.todo.models.User;

public interface ITodoService {
    public void addTodo(String title, String description, User user);

    public void updateTodo(String title, String newTitle, String description, User user);

    public void deleteTodo(String title, User user);
}
