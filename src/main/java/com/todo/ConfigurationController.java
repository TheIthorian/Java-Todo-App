package com.todo;

import java.sql.SQLException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationController implements IConfigurationController {

    private String username = null;
    private String password = null;
    private String databaseLocation = null;

    public static String configurationFilePath = "config.json";
    public static IFileHandler fileHandler = FileHandler.newInstance();

    // Nothing should happen in the constructor until a dependency class is made
    public ConfigurationController() {}

    public void setUsername(String username) {
        this.username = username != null ? username : this.username;
    }

    public void setPassword(String password) {
        this.password = password != null ? password : this.password;
    }

    public void setDatabaseLocation(String databaseLocation) {
        this.databaseLocation = databaseLocation != null ? databaseLocation : this.databaseLocation;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabaseLocation() {
        return this.databaseLocation;
    }

    public void saveToFile() {
        System.out.println("Saving configuration to file...");
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("username", this.username);
        dataMap.put("password", this.password);
        dataMap.put("databaseLocation", this.databaseLocation);

        JSONObject data = new JSONObject(dataMap);
        fileHandler.writeJSON(configurationFilePath, data);
    }

    public void load() {
        System.out.println("Loading configuration from file...");
        JSONObject config = fileHandler.readJSON(configurationFilePath);
        this.username = (String) getValue(config, "username");
        this.password = (String) getValue(config, "password");
        this.databaseLocation = (String) getValue(config, "databaseLocation");
    }

    public void addNewUser(String username, String password) {
        if (username == null) {
            System.out.println("Please provide a username for the new user.");
            return;
        }

        if (password == null) {
            System.out.println("Please provide a password for the new user.");
            return;
        }

        if (User.usernameExists(username)) {
            System.out.println("Username already exists.");
            return;
        }

        try {
            User newUser = new User(username, password);
            newUser.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Object getValue(JSONObject config, String key) {
        try {
            return config.get(key);
        } catch (JSONException e) {
            return null;
        }
    }
}
