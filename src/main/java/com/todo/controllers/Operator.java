package com.todo.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.todo.ArgumentCollection;
import com.todo.ArgumentParser;
import com.todo.ConfigurationValidator;
import com.todo.io.IOutputHandler;
import com.todo.models.TodoModel;

public class Operator {

    public ArgumentCollection arguments;
    public IOutputHandler outputHandler;

    public Operator(ArgumentCollection arguments, IOutputHandler outputHandler) {
        this.arguments = arguments;
        this.outputHandler = outputHandler;
    }

    public boolean processHelpOperation(ArgumentParser parser) {
        if (arguments.contains("-h")) {
            parser.printOptions();
            return true;
        }
        return false;
    }

    public boolean processConfigurationOperation(ConfigurationController configurationController,
            ConfigurationValidator validator, UserService userService) {
        if (arguments.contains("-cf")) {
            processConfigurationChange(configurationController);
            return true;
        }

        if (arguments.contains("-addUser")) {
            processAddUser(configurationController, validator, userService);
            return true;
        }

        return false;
    }

    private void processConfigurationChange(ConfigurationController configurationController) {
        configurationController.setUsername(arguments.get("username"));
        configurationController.setPassword(arguments.get("password"));
        configurationController.setDatabaseLocation(arguments.get("db"));
        configurationController.save();
    }

    private void printErrorsForConfigurationValidation(ConfigurationValidator validator) {
        Map<String, String> errors = validator.getErrors();
        for (String key : errors.keySet()) {
            outputHandler.write(key);
            outputHandler.write("Error: " + String.format(errors.get(key), key));
        }
    }

    private void processAddUser(ConfigurationController configurationController,
            ConfigurationValidator validator, UserService userService) {
        // Are we able to add a new user?
        if (!validator.isValid(configurationController)) {
            printErrorsForConfigurationValidation(validator);
            return;
        }

        try {
            userService.addUser(configurationController.getUsername(),
                    configurationController.getPassword());
            outputHandler.write("User successfully added.");
        } catch (UserService.UserValidationErrors.UsernameAlreadyExists e) {
            outputHandler.write("Username already exists.");
        } catch (UserService.UserValidationError e) {
            outputHandler.write("Unhandled validation error: " + e.getMessage());
        }

    }

    public boolean processTodoOperation(TodoService todoService) {
        if (!arguments.contains(Arrays.asList("-a", "-u", "-d", "-g"))) {
            return false;
        }

        if (arguments.contains("-a")) {
            todoService.addTodo(arguments.get("title"), arguments.get("description"));
        } else if (arguments.contains("-u")) {
            todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"));
        } else if (arguments.contains("-d")) {
            todoService.deleteTodo(arguments.get("title"));
        } else if (arguments.contains("-g")) {
            processGetTodo(todoService);
        }

        return true;
    }

    private void processGetTodo(TodoService todoService) {
        List<TodoModel> todos;

        if (arguments.contains("title")) {
            todos = todoService.getByTitle(arguments.get("title"));
        } else {
            todos = todoService.getAll();
        }

        for (TodoModel todo : todos) {
            if (todo.description != null) {
                outputHandler.write(String.format("[%s] :: %s :: %s", todo.getId(), todo.title,
                        todo.description));
            } else {
                outputHandler.write(String.format("[%s] :: %s\t", todo.getId(), todo.title));
            }
        }

        if (todos.size() == 0) {
            outputHandler.write("No todo items found.");
        }
    }

}
