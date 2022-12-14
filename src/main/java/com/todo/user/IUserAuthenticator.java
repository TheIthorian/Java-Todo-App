package com.todo.user;

import com.todo.models.User;

public interface IUserAuthenticator {
    /**
     * Save the user session details.
     * 
     * @param user - The user to be authenticated
     * @return true if the user was successfully authenticated
     */
    boolean authenticate(User user);

    /**
     * Returns a user matching the `username` and `password`. Returns `null` if no user is found.
     */
    User getUser(String username, String password);
}
