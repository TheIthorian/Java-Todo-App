package com.todo.operations;

import java.util.List;
import com.todo.ArgumentCollection;
import com.todo.controllers.TodoService;
import com.todo.models.TodoModel;

public class TodoOperation {
    private TodoService todoService;

    public TodoOperation(TodoService todoService) {
        this.todoService = todoService;
    }

    public void process(ArgumentCollection arguments) {
        if (arguments.contains("-a")) {
            String title = arguments.get("title");
            if (title == null) {
                System.out.println("Title is required.");
                return;
            }
            todoService.addTodo(title, arguments.get("description"));
            return;
        }

        if (arguments.contains("-u")) {
            todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"));
            System.out.println("Todo item updated.");
            return;
        }

        if (arguments.contains("-d")) {
            todoService.deleteTodo(arguments.get("title"));
            System.out.println("Todo item deleted.");
            return;
        }

        if (arguments.contains("-g")) {
            getTodoItems(arguments);
        }
    }

    private void getTodoItems(ArgumentCollection arguments) {
        List<TodoModel> todoItems =
                arguments.contains("title") ? todoService.getByTitle(arguments.get("title"))
                        : todoService.getAll();

        for (TodoModel todo : todoItems) {
            if (todo.description != null) {
                System.out.println(String.format("[%s] :: %s :: %s", todo.getId(), todo.title,
                        todo.description));
            } else {
                System.out.println(String.format("[%s] :: %s\t", todo.getId(), todo.title));
            }
        }

        if (todoItems.size() == 0) {
            System.out.println("No todo items found.");
        }
    }
}
