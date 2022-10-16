package com.todo.controllers;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.todo.DatabaseConfiguration;
import com.todo.IResourceHandler;

/**
 * The Configuration controller handles all accessing and setting local configuration.
 */
public class ConfigurationController {

    private String username = null;
    private String password = null;
    private String databaseLocation = null;
    private IResourceHandler resourceHandler;

    /**
     * Name of the json configuration file.
     */
    public String configurationFilePath = "config.json";

    public ConfigurationController(IResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

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
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseLocation() {
        return databaseLocation;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        return new DatabaseConfiguration(databaseLocation, "todo.db");
    }

    /**
     * Saves the current state of the configuration to file.
     */
    public void save() {
        System.out.println("Saving configuration to file...");
        JSONObject data = new JSONObject(buildDataMap());
        resourceHandler.writeJSON(configurationFilePath, data);
    }

    private HashMap<String, String> buildDataMap() {
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("username", username);
        dataMap.put("password", password);
        dataMap.put("databaseLocation", databaseLocation);
        return dataMap;
    }

    /**
     * Load configuration into memory using the set `configurationFilePath`.
     */
    public void load() {
        System.out.println("Loading configuration from file...");
        JSONObject config = resourceHandler.readJSON(configurationFilePath);
        username = (String) getValue(config, "username");
        password = (String) getValue(config, "password");
        databaseLocation = (String) getValue(config, "databaseLocation");
    }

    private Object getValue(JSONObject config, String key) {
        try {
            return config.get(key);
        } catch (JSONException e) {
            return null;
        }
    }
}
