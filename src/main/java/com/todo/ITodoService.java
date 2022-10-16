package com.todo;

import java.util.List;
import com.todo.models.TodoModel;

public interface ITodoService {
    /**
     * Returns a list of all todo items for the user.
     */
    public List<TodoModel> getAll();

    /*
     * Returns a list of existing todo items which match the input title.
     */
    public List<TodoModel> getByTitle(String title);

    /**
     * Adds a todo item with the given `title` and `description`.
     */
    public void addTodo(String title, String description);

    /**
     * Updates a todo item with the given `title` and changes the title and description values to
     * the `newTitle` and `newDescription` values.
     */
    public void updateTodo(String title, String newTitle, String description);

    /**
     * Deletes the todo item matching the given title.
     */
    public void deleteTodo(String title);
}
