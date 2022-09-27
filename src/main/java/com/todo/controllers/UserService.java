package com.todo.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import com.todo.Database;
import com.todo.models.User;
import com.todo.models.UserSelector;

public class UserService {
    static public void addUser(Database database, String username, String password) {
        try {
            Connection conn = database.connect();
            UserSelector userSelector = new UserSelector(conn);

            if (User.usernameExists(userSelector, username)) {
                System.out.println("Username already exists.");
                return;
            }

            User user = new User(username, password);
            user.insert(userSelector);
            conn.close();
        } catch (SQLException e) {
            System.out.print("Unable to add new user:");
            e.printStackTrace();
        }
    }

    static public User getUser(Database database, String username, String password) {
        System.out.println("getUser: " + username + " " + password);
        try {
            Connection conn = database.connect();
            UserSelector userSelector = new UserSelector(conn);
            User user = User.getByUsernamePassword(userSelector, username, password);
            conn.close();
            return user;
        } catch (SQLException e) {
            System.out.print("Unable to get user " + username);
            e.printStackTrace();
        }

        return null;
    }
}
