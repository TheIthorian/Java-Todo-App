package com.todo;

import java.util.Arrays;
import com.todo.controllers.ArgumentController;
import com.todo.controllers.ConfigurationController;
import com.todo.controllers.TodoService;
import com.todo.models.User;
import com.todo.operations.ConfigurationOperation;
import com.todo.operations.TodoOperation;
import com.todo.operations.TodoWizard;
import com.todo.user.IUserAuthenticator;
import com.todo.user.UserAuthenticator;

// https://www.oracle.com/java/technologies/javase/javadoc-tool.html
// https://www.baeldung.com/mockito-verify

public class AppController {
    public static ArgumentParser argumentParser = new ArgumentParser();

    public static IInputHandler inputHandler = new InputHandler();
    public static IResourceHandler fileHandler = new FileHandler();

    public static ConfigurationController configurationController;
    public static ConfigurationValidator configurationValidator = new ConfigurationValidator();

    public static DatabaseManager databaseManager;

    public static IUserAuthenticator userAuthenticator;

    public void run(String[] args) {
        ArgumentController.addOptions(argumentParser);

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
        if (new ConfigurationOperation(configurationController, databaseManager).process(arguments))
            return;

        // Do not further process arguments if configuration is invalid.
        ValidationResult configurationValidationResult =
                configurationValidator.validate(configurationController);
        if (!configurationValidationResult.isValid) {
            configurationValidator.printErrors(configurationValidationResult);
            return;
        }

        User user = userAuthenticator.getUser(configurationController.getUsername(),
                configurationController.getPassword());

        if (user == null)
            return;

        if (arguments.contains(Arrays.asList("-a", "-u", "-d", "-g"))) {
            TodoService todoService = new TodoService(databaseManager.getDatabase(), user);
            new TodoOperation(todoService).process(arguments);
            return;
        }

        new TodoWizard().process();
    }

    public boolean processHelpOperation(ArgumentCollection arguments, ArgumentParser parser) {
        if (arguments.contains("-h")) {
            parser.printOptions();
            return true;
        }
        return false;
    }

    public void terminate() {
        inputHandler.awaitInput("Prees enter to close...");
    }
}
