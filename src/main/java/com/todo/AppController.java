package com.todo;

import java.util.Arrays;
import java.util.List;
import com.todo.controllers.ArgumentController;
import com.todo.controllers.ConfigurationController;
import com.todo.controllers.TodoService;
import com.todo.controllers.UserService;
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

        if (user == null)
            return;

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
                return true;
            }

            UserService userService = new UserService(databaseManager.getDatabase());
            try {
                userService.addUser(configurationController.getUsername(),
                        configurationController.getPassword());
            } catch (UserService.UserValidationErrors.UsernameAlreadyExists e) {
                System.out.println("Username already exists.");
            } catch (UserService.UserValidationError e) {
                System.out.println("Unhandled validation error: " + e.getMessage());
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
                    System.out.println(String.format("[%s] :: %s :: %s", todo.getId(), todo.title,
                            todo.description));
                } else {
                    System.out.println(String.format("[%s] :: %s\t", todo.getId(), todo.title));
                }
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
