package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends UserSelector.UserDto {
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(UserSelector.UserDto user) {
        super(user);
    }

    public static void createTable(Connection conn) throws SQLException {
        final String createUsers = "CREATE TABLE IF NOT EXISTS users "
                + "(userId integer PRIMARY KEY, username text NOT NULL, password text NOT NULL)";

        Statement statement = conn.createStatement();
        statement.execute(createUsers);
        statement.close();
    }
}
