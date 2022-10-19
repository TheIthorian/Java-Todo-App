package com.todo;

import com.todo.controllers.ArgumentController;
import com.todo.controllers.ConfigurationController;
import com.todo.controllers.Operator;
import com.todo.controllers.TodoService;
import com.todo.controllers.UserService;
import com.todo.io.IInputHandler;
import com.todo.io.IOutputHandler;
import com.todo.io.InputHandler;
import com.todo.io.OutputHandler;
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
        Operator operator = new Operator(arguments, outputHandler);

        // Do not further process arguments if help is present.
        if (operator.processHelpOperation(argumentParser))
            return;

        // Do not further process arguments if configuration is changed.
        UserService userService = new UserService(databaseManager.getDatabase());
        if (operator.processConfigurationOperation(configurationController, configurationValidator,
                userService))
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

        TodoService todoService = new TodoService(databaseManager.getDatabase(), user);
        operator.processTodoOperation(todoService);
        processNoOperation(arguments, user);
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
        inputHandler.awaitInput("\nPrees enter to close...");
    }
}
