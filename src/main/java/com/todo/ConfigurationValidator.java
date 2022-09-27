package com.todo;

import java.util.HashMap;

public class ConfigurationValidator {

    private String NOT_DEFINED = "%s is not specified in ";
    private HashMap<String, String> errors = new HashMap<String, String>();

    public boolean isValid(ConfigurationController configurationController) {
        if (configurationController.getDatabaseLocation() == null) {
            errors.put("databaseLocation",
                    NOT_DEFINED + configurationController.configurationFilePath);
        }
        if (configurationController.getUsername() == null) {
            errors.put("username", NOT_DEFINED + configurationController.configurationFilePath);
        }
        if (configurationController.getPassword() == null) {
            errors.put("password", NOT_DEFINED + configurationController.configurationFilePath);
        }

        printErrors();

        return errors.isEmpty();
    }

    private void printErrors() {
        for (String key : errors.keySet()) {
            System.out.println("Error: " + String.format(errors.get(key), key));
        }
    }
}
