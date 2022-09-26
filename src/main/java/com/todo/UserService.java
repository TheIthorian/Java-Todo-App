package com.todo;

import java.sql.SQLException;
import com.todo.models.User;
import com.todo.models.UserSelector;

public class UserService {
    static void addUser(Database database, String username, String password) {
        try {
            UserSelector userSelector = new UserSelector(database.connect());
            User.addUser(userSelector, username, password);
            conn.close();
        } catch (SQLException e) {
            System.out.print("Unable to add new user:");
            e.printStackTrace();
        }
    }

    static User getUser(Database database, String username, String password) {
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
