package com.todo.controllers;

import java.sql.SQLException;
import com.todo.AbstractDatabase;
import com.todo.models.User;
import com.todo.models.UserSelector;
import com.todo.models.UserSelector.UserDto;
import com.todo.util.Hasher;
import com.todo.util.IHasher;

/**
 * Methods used to perform CRUD operations on the stored users.
 */
public class UserService {
    private AbstractDatabase database;

    public static abstract class UserValidationError extends Exception {}
    public static class UserValidationErrors {
        public static class UsernameAlreadyExists extends UserValidationError {}
    }

    public IHasher passwordHasher;
    public UserSelector selector;

    public UserService(AbstractDatabase database) {
        this.database = database;
        this.passwordHasher = new Hasher();
        this.selector = new UserSelector();
    }

    /**
     * Returns `true` if the username and password combination matches with an existing user.
     */
    public boolean isPasswordCorrect(String username, String password) throws SQLException {
        UserDto user = selector.selectByUsername(username);
        return passwordHasher.matches(password, user.password);
    }

    /**
     * Returns a user with the matching `username` and `password`.
     */
    public User getUser(String username, String password) {
        try {
            selector.connect(database);
            UserSelector.UserDto user = selector.selectByUsername(username);
            selector.disconnect();

            if (user == null || !passwordHasher.matches(password, user.password)) {
                return null;
            }

            return new User(user);
        } catch (SQLException e) {
            System.out.print("Unable to get user " + username);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds a user with the given `username` and `password`.
     */
    public void addUser(String username, String password) throws UserValidationError {
        try {
            selector.connect(database);

            if (selector.selectByUsername(username) != null) {
                selector.disconnect();
                throw new UserValidationErrors.UsernameAlreadyExists();
            }

            User user = new User(username, hashPassword(password));
            selector.insert(user);
            selector.disconnect();

        } catch (SQLException e) {
            System.out.print("Unable to add new user:");
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        String salt = passwordHasher.salt();
        return passwordHasher.hash(password, salt);
    }

}
