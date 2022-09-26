package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends UserSelector.UserDto {
    private boolean isAuthenticated = false;

    User(UserSelector.UserDto user) {
        super(user);
        this.isAuthenticated = false;
    }

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    int getId() {
        return this.userId;
    }

    boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    boolean authenticate() {
        this.isAuthenticated = true;
        return this.isAuthenticated;
    }

    boolean isPasswordCorrect(UserSelector selector) {
        return isPasswordCorrect(selector, this.username, this.password);
    }

    void insert(UserSelector selector) throws SQLException {
        selector.insert(this);
    }

    public static boolean isPasswordCorrect(UserSelector selector, String username,
            String password) {
        return (selector.selectByUsernamePassword(username, password) == null);
    }

    public static User getByUsernamePassword(UserSelector selector, String username,
            String password) {
        UserSelector.UserDto user = selector.selectByUsernamePassword(username, password);
        if (user != null) {
            return new User(user);
        }
        return null;
    }

    public static boolean usernameExists(UserSelector selector, String username) {
        return selector.selectByUsername(username) != null;
    }

    public static void createTable(Connection conn) throws SQLException {
        String createUsers = "CREATE TABLE IF NOT EXISTS users "
                + "(userId integer PRIMARY KEY, username text NOT NULL, password text NOT NULL)";

        Statement statement = conn.createStatement();
        statement.execute(createUsers);
        statement.close();
    }

    public static User addUser(UserSelector selector, String username, String password) {
        if (usernameExists(selector, username)) {
            System.out.println("Username already exists.");
            return null;
        }

        User newUser = new User(username, password);
        return new User(selector.insert(newUser));
    }
}
