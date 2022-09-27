package com.todo;

import java.util.HashMap;

public class ConfigurationValidator {

    private String NOT_DEFINED = "is not specified in config.json.";
    private HashMap<String, String> errors = new HashMap<String, String>();

    public boolean isValid(ConfigurationController configurationController) {
        if (configurationController.getDatabaseLocation() == null) {
            errors.put("databaseLocation", NOT_DEFINED);
        }
        if (configurationController.getUsername() == null) {
            errors.put("username", NOT_DEFINED);
        }
        if (configurationController.getPassword() == null) {
            errors.put("password", NOT_DEFINED);
        }

        printErrors();

        return errors.isEmpty();
    }

    private void printErrors() {
        for (String key : errors.keySet()) {
            System.out.println("Error: " + key + " " + NOT_DEFINED);
        }
    }
}
