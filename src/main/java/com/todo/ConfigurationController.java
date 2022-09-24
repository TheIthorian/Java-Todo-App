package com.todo;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationController implements IConfigurationController {

    private String username = null;
    private String password = null;

    public static String configurationFilePath = "config.json";
    public static IFileHandler fileHandler = FileHandler.newInstance();

    public ConfigurationController() {
        this.load(configurationFilePath);
    }

    public void setUsername(String username) {
        this.username = username != null ? username : this.username;
    }

    public void setPassword(String password) {
        this.password = password != null ? password : this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void saveToFile() {
        System.out.println("Saving configuration to file...");
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("username", this.username);
        dataMap.put("password", this.password);

        JSONObject data = new JSONObject(dataMap);
        FileHandler.writeJSON(this.configurationFilePath, data);
    }

    public void load(String pathname) {
        System.out.println("Loading configuration from file...");
        JSONObject config = FileHandler.readJSON(pathname);
        this.username = (String) getValue(config, "username");
        this.password = (String) getValue(config, "password");
    }

    private Object getValue(JSONObject config, String key) {
        try {
            return config.get(key);
        } catch (JSONException e) {
            return null;
        }
    }
}
