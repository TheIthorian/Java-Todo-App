package com.todo;

public interface ITodoService {
    public TodoModel addTodo(String title, String description, User user);

    public TodoModel updateTodo(String title, String newTitle, String description, User user);

    public void deleteTodo(String title, User user);
}
