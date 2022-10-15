package com.todo.controllers;

import java.sql.SQLException;
import com.todo.AbstractDatabase;
import com.todo.models.User;
import com.todo.models.UserSelector;

/**
 * Methods used to perform CRUD operations on the stored users.
 */
public class UserService {
    private AbstractDatabase database;

    public UserSelector selector;

    public UserService(AbstractDatabase database) {
        this.database = database;
        this.selector = new UserSelector();
    }

    /**
     * Returns `true` if the username and password combination matches with an existing user.
     */
    public boolean isPasswordCorrect(String username, String password) throws SQLException {
        return (selector.selectByUsernamePassword(username, password) != null);
    }

    /**
     * Adds a user with the given `username` and `password`.
     */
    public void addUser(String username, String password) {
        try {
            selector.connect(database);

            if (selector.selectByUsername(username) != null) {
                System.out.println("Username already exists.");
                return;
            }

            User user = new User(username, password);
            selector.insert(user);

            selector.disconnect();

        } catch (SQLException e) {
            System.out.print("Unable to add new user:");
            e.printStackTrace();
        }
    }

    /**
     * Returns a user with the matching `username` and `password`.
     */
    public User getUser(String username, String password) {
        System.out.println("getUser: " + username + " " + password);
        try {
            selector.connect(database);
            UserSelector.UserDto user = selector.selectByUsernamePassword(username, password);
            selector.disconnect();

            if (user != null) {
                return new User(user);
            }

            return null;

        } catch (SQLException e) {
            System.out.print("Unable to get user " + username);
            e.printStackTrace();
        }

        return null;
    }
}
