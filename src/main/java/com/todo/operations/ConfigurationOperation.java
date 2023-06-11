package com.todo.operations;

import com.todo.ArgumentCollection;
import com.todo.ConfigurationValidator;
import com.todo.DatabaseManager;
import com.todo.controllers.ConfigurationController;
import com.todo.controllers.UserService;

public class ConfigurationOperation {
    private ConfigurationController configurationController;
    private DatabaseManager databaseManager;

    public ConfigurationOperation(ConfigurationController configurationController,
            DatabaseManager databaseManager) {
        this.configurationController = configurationController;
        this.databaseManager = databaseManager;
    }

    public boolean process(ArgumentCollection arguments) {
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
            if (!validator.validate(configurationController).isValid) {
                return true;
            }

            UserService userService = new UserService(databaseManager.getDatabase());
            try {
                userService.addUser(configurationController.getUsername(),
                        configurationController.getPassword());
                System.out.println("User: '" + configurationController.getUsername() + "'' added.");
            } catch (UserService.UserValidationErrors.UsernameAlreadyExists e) {
                System.out.println("Username already exists.");
            } catch (UserService.UserValidationError e) {
                System.out.println("Unhandled validation error: " + e.getMessage());
            }

            return true;
        }

        return false;
    }
}
