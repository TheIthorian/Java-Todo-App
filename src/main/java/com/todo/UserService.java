package com.todo;

import java.sql.SQLException;
import com.todo.models.User;
import com.todo.models.UserSelector;

public class UserService {
    static void addUser(Database database, String username, String password) {
        try {
            UserSelector userSelector = new UserSelector(database.connect());
            User.addUser(userSelector, username, password);
        } catch (SQLException e) {
            System.out.print("Unable to add new user:");
            e.printStackTrace();
        }
    }
}
