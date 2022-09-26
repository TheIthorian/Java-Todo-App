package com.todo;

import java.sql.SQLException;
import com.todo.models.User;

// https://www.oracle.com/java/technologies/javase/javadoc-tool.html
// https://www.baeldung.com/mockito-verify

public class TodoController {
    public static IArgumentParser argumentParser = new ArgumentParser();
    public static IInputHandler inputHandler = new InputHandler();
    public static ITodoService todoService = new TodoService();
    public static IConfigurationController configurationController = new ConfigurationController();
    public static ConfigurationValidator configurationValidator = new ConfigurationValidator();
    public static UserAuthenticator userAuthenticator = new UserAuthenticator();

    public TodoController() {}

    public void run(String[] args) {
        ArgumentCollection arguments = argumentParser.readArgs(args);
        configurationController.load();

        this.buildDatabase();

        this.handleConfigurationOperation(arguments);

        this.handleTodoOperation(arguments);

        this.handleNoOperation(arguments);

        inputHandler.awaitInput("Prees enter to close...");
    }

    private void buildDatabase() {
        if (!Database.alreadyExists()) {
            Database.createTables();
        }
    }

    private void handleConfigurationOperation(ArgumentCollection arguments) {
        if (arguments.contains("-cf")) {
            configurationController.setUsername(arguments.get("username"));
            configurationController.setPassword(arguments.get("password"));
            configurationController.setDatabaseLocation(arguments.get("db"));
            configurationController.saveToFile();
        }

        if (arguments.contains("-ca")) {
            configurationController.addNewUser(arguments.get("username"),
                    arguments.get("password"));
        }
    }

    private void handleTodoOperation(ArgumentCollection arguments) {
        if (!arguments.contains("-a") && !arguments.contains("-u") && !arguments.contains("-d")) {
            return;
        }

        if (!configurationValidator.isValid(configurationController)) {
            return;
        }

        if (!userAuthenticator.areCredentialsCorrect(configurationController)) {
            System.out.println("Credentials not recognised.");
            return;
        }

        User user = null;
        try {
            user = User.getByUsernamePassword(configurationController.getUsername(),
                    configurationController.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (arguments.contains("-a")) {
            todoService.addTodo(arguments.get("title"), arguments.get("description"), user);
        } else if (arguments.contains("-u")) {
            todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"), user);
        } else if (arguments.contains("-d")) {
            todoService.deleteTodo(arguments.get("title"), user);
        }
    }

    private void handleNoOperation(ArgumentCollection arguments) {
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
