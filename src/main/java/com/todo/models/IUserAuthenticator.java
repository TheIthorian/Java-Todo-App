package com.todo.models;

public interface IUserAuthenticator {
    /**
     * Save the user session details
     * 
     * @param user - The user to be authenticated
     * @return true if the user was successfully authenticated
     */
    public boolean authenticate(User user);
}
