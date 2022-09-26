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

    static boolean isPasswordCorrect(UserSelector selector, String username, String password) {
        return (selector.selectByUsernamePassword(username, password) == null);
    }

    static User getByUsernamePassword(UserSelector selector, String username, String password) {
        UserSelector.UserDto user = selector.selectByUsernamePassword(username, password);
        if (user != null) {
            return new User(user);
        }
        return null;
    }

    static boolean usernameExists(UserSelector selector, String username) {
        return selector.selectByUsername(username) != null;
    }

    static void createTable(Connection conn) throws SQLException {
        String createUsers = "CREATE TABLE IF NOT EXISTS users "
                + "(userId integer PRIMARY KEY, username text NOT NULL, password text NOT NULL)";

        Statement statement = conn.createStatement();
        statement.execute(createUsers);
        statement.close();
    }

}
