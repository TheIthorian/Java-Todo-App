package com.todo;

import com.todo.controllers.ConfigurationController;

/**
 * Class to validate tha the local configuration is valid.
 */
public class ConfigurationValidator {
    private String NOT_DEFINED = "%s is not specified in ";

    public ValidationResult validate(ConfigurationController configurationController) {
        ValidationResult validationResult = ValidationResult.success();

        if (configurationController.getDatabaseLocation() == null) {
            validationResult.addError(
                    formatError(NOT_DEFINED + configurationController.configurationFilePath,
                            "databaseLocation"));
        }

        if (configurationController.getUsername() == null) {
            validationResult.addError(formatError(
                    NOT_DEFINED + configurationController.configurationFilePath, "username"));
        }

        if (configurationController.getPassword() == null) {
            validationResult.addError(formatError(
                    NOT_DEFINED + configurationController.configurationFilePath, "password"));
        }

        return validationResult;

    }

    public void printErrors(ValidationResult validationResult) {
        for (String error : validationResult.errors) {
            System.out.println("Error: " + error);
        }
    }

    private String formatError(String format, String value) {
        return String.format(format, value);
    }
}
