package com.todo;

import java.util.HashMap;
import org.json.JSONObject;

public class ConfigurationController implements IConfigurationController {

    private String username = null;
    private String password = null;
    private String configurationFilePath = "config.json";

    public ConfigurationController() {}

    public void setUsername(String username) {
        System.out.println("Your username is: " + username);
        this.username = username;
    }

    public void setPassword(String password) {
        System.out.println("Your password is: " + password);
        this.password = password;
    }

    public void saveToFile() {
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("username", this.username);
        dataMap.put("password", this.password);

        JSONObject data = new JSONObject(dataMap);
        FileHandler.writeJSON(this.configurationFilePath, data);
    }

    public void load(String pathname) {
        JSONObject config = FileHandler.readJSON(pathname);
        this.username = (String) config.get("username");
        this.password = (String) config.get("password");
    }
}
