package com.todo;

public interface IConfigurationController {
    public void setUsername(String username);

    public void setPassword(String password);

    public String getUsername();

    public String getPassword();

    public void saveToFile();

    public void load(String pathname);
}
