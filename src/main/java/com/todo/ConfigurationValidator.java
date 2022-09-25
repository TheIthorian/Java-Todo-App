package com.todo;

public class ConfigurationValidator {
    public boolean isValid(IConfigurationController configurationController) {
        if (configurationController.getDatabaseLocation() == null) {
            System.out.println("No database location is specified in config.json.");
            return false;
        }
        if (configurationController.getUsername() == null
                || configurationController.getPassword() == null) {
            System.out.println("No database location is specified in config.json.");
            return false;
        }
        return true;
    }
}
