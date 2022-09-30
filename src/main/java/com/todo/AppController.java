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

    public static IUserAuthenticator userAuthenticator = new UserAuthenticator();

    public static ConfigurationController configurationController =
            new ConfigurationController(fileHandler);
    public static ConfigurationValidator configurationValidator;

    // public static UserAuthenticator userAuthenticator = new UserAuthenticator();

    public static Database database;

    public TodoService todoService = new TodoService(null, null);

    public AppController() {}

    public void run(String[] args) {
        ArgumentCollection arguments = argumentParser.readArgs(args);
        configurationValidator = new ConfigurationValidator();
        configurationController.load(); // configuration is empty until we read load from somewhere
        database = new Database(configurationController.getDatabaseConfiguration(), fileHandler);

        buildDatabase();
        processArguments(arguments);

        inputHandler.awaitInput("Prees enter to close...");
    }

    public void buildDatabase() {
        if (!database.alreadyExists()) {
            database.createTables();
        }
    }

    public void processArguments(ArgumentCollection arguments) {
        User user = getUser(database, configurationController);
        if (user == null) {
            return;
        }
        System.out.println(user.getId() + " : " + user.username);

        processTodoOperation(arguments, new TodoService(database, user));
        processNoOperation(arguments, user);
        processConfigurationOperation(arguments);
    }

    public User getUser(Database database, ConfigurationController configurationController) {
        if (!configurationValidator.isValid(configurationController)) {
            return null;
        }
        User user = UserService.getUser(database, configurationController.getUsername(),
                configurationController.getPassword());

        if (user == null || !user.authenticate(userAuthenticator)) {
            System.out.println("Credentials not recognised.");
            return null;
        }

        return user;
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

            UserService.addUser(database, configurationController.getUsername(),
                    configurationController.getPassword());
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
}
