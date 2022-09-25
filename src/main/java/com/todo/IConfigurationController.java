package com.todo;

public interface IConfigurationController {
    public void setUsername(String username);

    public void setPassword(String password);

    public void setDatabaseLocation(String databaseLocation);

    public String getUsername();

    public String getPassword();

    public String getDatabaseLocation();

    public void addNewUser(String username, String password);

    public void saveToFile();

    public void load();
}
