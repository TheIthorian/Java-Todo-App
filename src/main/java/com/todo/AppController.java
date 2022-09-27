package com.todo;

import com.todo.models.User;

// https://www.oracle.com/java/technologies/javase/javadoc-tool.html
// https://www.baeldung.com/mockito-verify

public class AppController {
    public static ArgumentParser argumentParser = new ArgumentParser();
    public static IInputHandler inputHandler = new InputHandler();
    public static IResourceHandler fileHandler = new FileHandler();

    public static ConfigurationController configurationController =
            new ConfigurationController(fileHandler);
    public static ConfigurationValidator configurationValidator;

    // public static UserAuthenticator userAuthenticator = new UserAuthenticator();

    public static Database database;

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

    private void buildDatabase() {
        if (!database.alreadyExists()) {
            database.createTables();
        }
    }

    private void processArguments(ArgumentCollection arguments) {
        processTodoOperation(arguments);
        processNoOperation(arguments);
        processConfigurationOperation(arguments);
    }

    private void processConfigurationOperation(ArgumentCollection arguments) {
        if (arguments.contains("-cf")) {
            configurationController.setUsername(arguments.get("username"));
            configurationController.setPassword(arguments.get("password"));
            configurationController.setDatabaseLocation(arguments.get("db"));
            configurationController.saveToFile();
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

    private void processTodoOperation(ArgumentCollection arguments) {
        if (!arguments.contains("-a") && !arguments.contains("-u") && !arguments.contains("-d")) {
            return;
        }

        if (!configurationValidator.isValid(configurationController)) {
            return;
        }

        User user = UserService.getUser(database, configurationController.getUsername(),
                configurationController.getPassword());

        if (user == null || !user.authenticate()) {
            System.out.println("Credentials not recognised.");
            return;
        }

        TodoService todoService = new TodoService(database);

        if (arguments.contains("-a")) {
            todoService.addTodo(arguments.get("title"), arguments.get("description"), user);
        } else if (arguments.contains("-u")) {
            todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"), user);
        } else if (arguments.contains("-d")) {
            todoService.deleteTodo(arguments.get("title"), user);
        }
    }

    private void processNoOperation(ArgumentCollection arguments) {
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
