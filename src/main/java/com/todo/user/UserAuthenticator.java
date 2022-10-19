package com.todo.user;

import com.todo.AbstractDatabase;
import com.todo.controllers.UserService;
import com.todo.models.User;

public class UserAuthenticator implements IUserAuthenticator {
    public AbstractDatabase database;
    public UserService userService;

    public UserAuthenticator(AbstractDatabase database) {
        this.database = database;
        this.userService = new UserService(database);
    }

    public boolean authenticate(User user) {
        return true;
    }

    public User getUser(String username, String password) {
        User user = userService.getUser(username, password);

        if (user == null || !authenticate(user)) {
            return null;
        }

        return user;
    }
}
