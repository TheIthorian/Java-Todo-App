package com.todo;

import java.util.Arrays;
import java.util.List;
import com.todo.controllers.ConfigurationController;
import com.todo.controllers.TodoService;
import com.todo.controllers.UserService;
import com.todo.models.TodoModel;
import com.todo.models.User;

// https://www.oracle.com/java/technologies/javase/javadoc-tool.html
// https://www.baeldung.com/mockito-verify

public class AppController {
    public static ArgumentParser argumentParser = new ArgumentParser();
    public static IInputHandler inputHandler = new InputHandler();

    public static IResourceHandler fileHandler = new FileHandler();

    public static ConfigurationController configurationController =
            new ConfigurationController(fileHandler);
    public static ConfigurationValidator configurationValidator = new ConfigurationValidator();

    public static Database database =
            new Database(configurationController.getDatabaseConfiguration(), fileHandler);

    public static IUserAuthenticator userAuthenticator = new UserAuthenticator(database);

    public AppController() {}

    public void run(String[] args) {
        loadConfiguration();
        if (!isConfigurationValid())
            return;

        buildDatabase();

        ArgumentCollection arguments = readArguments(args);
        processArguments(arguments);

        terminate();
    }

    private void loadConfiguration() {
        configurationController.load(); // configuration is empty until we read load from somewhere
    }

    private boolean isConfigurationValid() {
        return !configurationValidator.isValid(configurationController);
    }

    public void buildDatabase() {
        if (!database.alreadyExists()) {
            database.createTables();
        }
    }

    private ArgumentCollection readArguments(String[] args) {
        return argumentParser.readArgs(args);
    }

    public void processArguments(ArgumentCollection arguments) {
        User user = userAuthenticator.getUser(configurationController.getUsername(),
                configurationController.getPassword());

        if (user == null)
            return;

        System.out.println(user.getId() + " : " + user.username);

        processTodoOperation(arguments, new TodoService(database, user));
        processNoOperation(arguments, user);
        processConfigurationOperation(arguments);
    }

    public void processConfigurationOperation(ArgumentCollection arguments) {
        if (arguments.contains("-cf")) {
            configurationController.setUsername(arguments.get("username"));
            configurationController.setPassword(arguments.get("password"));
            configurationController.setDatabaseLocation(arguments.get("db"));
            configurationController.save();
        }

        if (arguments.contains("-addUser")) {
            // Are we able to add a new user?
            ConfigurationValidator validator = new ConfigurationValidator();
            if (!validator.isValid(configurationController)) {
                return;
            }

            UserService userService = new UserService(database);
            try {
                userService.addUser(configurationController.getUsername(),
                        configurationController.getPassword());
            } catch (UserService.UserValidationErrors.UsernameAlreadyExists e) {
                System.out.println("Username already exists.");
            } catch (UserService.UserValidationError e) {
                System.out.println("Unhandled validation error: " + e.getMessage());
            }
        }
    }

    public void processTodoOperation(ArgumentCollection arguments, TodoService todoService) {
        if (!arguments.contains(Arrays.asList("-a", "-u", "-d", "-g"))) {
            return;
        }

        System.out.println(arguments.get("-g"));

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
                System.out.println(todo.getId() + "\t: " + todo.title + " : " + todo.description);
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
