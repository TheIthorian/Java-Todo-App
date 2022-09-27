package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends UserSelector.UserDto {
    private boolean isAuthenticated = false;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private User(UserSelector.UserDto user) {
        super(user);
        this.isAuthenticated = false;
    }

    public int getId() {
        return this.userId;
    }

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public boolean authenticate() {
        this.isAuthenticated = true;
        return this.isAuthenticated;
    }

    public boolean isPasswordCorrect(UserSelector selector) {
        return isPasswordCorrect(selector, this.username, this.password);
    }

    public void insert(UserSelector selector) {
        selector.insert(this);
    }

    public void update(UserSelector selector) {
        throw new UnsupportedOperationException("User.update is not yet implemented");
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
