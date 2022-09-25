package com.todo;

import java.sql.SQLException;

public class UserAuthenticator {
    public boolean areCredentialsCorrect(IConfigurationController configurationController) {
        try {
            return User.isPasswordCorrect(configurationController.getUsername(),
                    configurationController.getPassword());
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
