package com.todo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.todo.controllers.ArgumentController;
import com.todo.controllers.ConfigurationController;
import com.todo.controllers.TodoService;
import com.todo.controllers.UserService;
import com.todo.io.IInputHandler;
import com.todo.io.IOutputHandler;
import com.todo.io.InputHandler;
import com.todo.io.OutputHandler;
import com.todo.models.TodoModel;
import com.todo.models.User;
import com.todo.user.IUserAuthenticator;
import com.todo.user.UserAuthenticator;

// https://www.oracle.com/java/technologies/javase/javadoc-tool.html
// https://www.baeldung.com/mockito-verify

public class AppController {
    public static ArgumentParser argumentParser = new ArgumentParser();
    public static ArgumentController argumentController = new ArgumentController(argumentParser);

    public static IInputHandler inputHandler = new InputHandler();
    public static IOutputHandler outputHandler = new OutputHandler();

    public static IResourceHandler fileHandler = new FileHandler();

    public static ConfigurationController configurationController;
    public static ConfigurationValidator configurationValidator = new ConfigurationValidator();

    public static DatabaseManager databaseManager;

    public static IUserAuthenticator userAuthenticator;

    public void run(String[] args) {
        configurationController = new ConfigurationController(fileHandler);
        configurationController.load(); // configuration is empty until we read load from somewhere.

        databaseManager = new DatabaseManager(configurationController, fileHandler);
        userAuthenticator = new UserAuthenticator(databaseManager.getDatabase());

        processArguments(args);

        terminate();
    }

    public void processArguments(String[] args) {

        ArgumentCollection arguments = argumentParser.readArgs(args);

        // Do not further process arguments if help is present.
        if (processHelpOperation(arguments, argumentParser))
            return;

        // Do not further process arguments if configuration is changed.
        if (processConfigurationOperation(arguments))
            return;

        // Do not further process arguments if configuration is invalid.
        if (!configurationValidator.isValid(configurationController))
            return;

        User user = userAuthenticator.getUser(configurationController.getUsername(),
                configurationController.getPassword());

        if (user == null) {
            outputHandler.write("Credentials not recognised");
            return;
        }

        processTodoOperation(arguments, new TodoService(databaseManager.getDatabase(), user));
        processNoOperation(arguments, user);
    }

    public boolean processHelpOperation(ArgumentCollection arguments, ArgumentParser parser) {
        if (arguments.contains("-h")) {
            parser.printOptions();
            return true;
        }
        return false;
    }

    public boolean processConfigurationOperation(ArgumentCollection arguments) {
        if (arguments.contains("-cf")) {
            configurationController.setUsername(arguments.get("username"));
            configurationController.setPassword(arguments.get("password"));
            configurationController.setDatabaseLocation(arguments.get("db"));
            configurationController.save();
            return true;
        }

        if (arguments.contains("-addUser")) {
            // Are we able to add a new user?
            ConfigurationValidator validator = new ConfigurationValidator();
            if (!validator.isValid(configurationController)) {
                Map<String, String> errors = validator.getErrors();
                for (String key : errors.keySet()) {
                    outputHandler.write(key);
                    outputHandler.write("Error: " + String.format(errors.get(key), key));
                }
                return true;
            }

            UserService userService = new UserService(databaseManager.getDatabase());
            try {
                userService.addUser(configurationController.getUsername(),
                        configurationController.getPassword());
            } catch (UserService.UserValidationErrors.UsernameAlreadyExists e) {
                outputHandler.write("Username already exists.");
            } catch (UserService.UserValidationError e) {
                outputHandler.write("Unhandled validation error: " + e.getMessage());
            }

            return true;
        }

        return false;
    }

    public void processTodoOperation(ArgumentCollection arguments, TodoService todoService) {
        if (!arguments.contains(Arrays.asList("-a", "-u", "-d", "-g"))) {
            return;
        }

        if (arguments.contains("-a")) {
            todoService.addTodo(arguments.get("title"), arguments.get("description"));
        } else if (arguments.contains("-u")) {
            todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"));
        } else if (arguments.contains("-d")) {
            todoService.deleteTodo(arguments.get("title"));
        } else if (arguments.contains("-g")) {

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

    private void processNoOperation(ArgumentCollection arguments, User user) {
        if (!arguments.isEmpty()) {
            return;
        }

        // Start the application.
        // Walk through adding, updating etc

        /*
         * Select option: Find / All / Add / Update / Remove
         */
    }

    public void terminate() {
        inputHandler.awaitInput("Prees enter to close...");
    }
}
