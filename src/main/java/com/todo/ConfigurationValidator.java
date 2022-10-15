package com.todo;

import java.util.HashMap;
import java.util.Map;
import com.todo.controllers.ConfigurationController;

/**
 * Class to validate tha the local configuration is valid.
 */
public class ConfigurationValidator {

    private String NOT_DEFINED = "%s is not specified in ";
    private HashMap<String, String> errors = new HashMap<String, String>();

    /**
     * Returns `false` and prints any validation errors if there is any local misconfiguration.
     */
    public boolean isValid(ConfigurationController configurationController) {
        if (configurationController.getDatabaseLocation() == null) {
            errors.put("databaseLocation",
                    formatError(NOT_DEFINED + configurationController.configurationFilePath,
                            "databaseLocation"));
        }
        if (configurationController.getUsername() == null) {
            errors.put("username", formatError(
                    NOT_DEFINED + configurationController.configurationFilePath, "username"));
        }
        if (configurationController.getPassword() == null) {
            errors.put("password", formatError(
                    NOT_DEFINED + configurationController.configurationFilePath, "password"));
        }

        printErrors();

        return errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    private void printErrors() {
        for (String key : errors.keySet()) {
            System.out.println("Error: " + String.format(errors.get(key), key));
        }
    }

    private String formatError(String format, String value) {
        return String.format(format, value);
    }
}
