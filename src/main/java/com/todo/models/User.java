package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.todo.IUserAuthenticator;

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

    public boolean authenticate(IUserAuthenticator authenticator) {
        this.isAuthenticated = authenticator.authenticate(this);
        return this.isAuthenticated;
    }

    public void insert(UserSelector selector) throws SQLException {
        selector.insert(this);
    }

    public void update(UserSelector selector) throws SQLException {
        throw new UnsupportedOperationException("User.update is not yet implemented");
    }

    public boolean isPasswordCorrect(UserSelector selector) throws SQLException {
        return isPasswordCorrect(selector, this.username, this.password);
    }

    public static boolean isPasswordCorrect(UserSelector selector, String username, String password)
            throws SQLException {
        return (selector.selectByUsernamePassword(username, password) == null);
    }

    public static boolean usernameExists(UserSelector selector, String username)
            throws SQLException {
        return selector.selectByUsername(username) != null;
    }

    public static User getByUsernamePassword(UserSelector selector, String username,
            String password) throws SQLException {
        UserSelector.UserDto user = selector.selectByUsernamePassword(username, password);
        if (user != null) {
            return new User(user);
        }
        return null;
    }

    public static void createTable(Connection conn) throws SQLException {
        String createUsers = "CREATE TABLE IF NOT EXISTS users "
                + "(userId integer PRIMARY KEY, username text NOT NULL, password text NOT NULL)";

        Statement statement = conn.createStatement();
        statement.execute(createUsers);
        statement.close();
    }
}
