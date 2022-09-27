package com.todo;

import com.todo.models.User;

public class UserAuthenticator implements IUserAuthenticator {
    public boolean authenticate(User user) {
        return true;
    }
}
