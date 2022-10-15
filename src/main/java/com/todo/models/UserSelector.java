package com.todo.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSelector extends BaseSelector {

    public static class UserDto {
        protected int userId;
        public String username;
        public String password;

        UserDto() {}

        UserDto(ResultSet result) throws SQLException {
            this.userId = result.getInt("userId");
            this.username = result.getString("username");
            this.password = result.getString("password");
        }

        UserDto(int userId, String username, String password) {
            this.userId = userId;
            this.username = username;
            this.password = password;
        }

        UserDto(UserDto user) {
            this.userId = user.userId;
            this.username = user.username;
            this.password = user.password;
        }

        public int getId() {
            return this.userId;
        }
    }

    public UserSelector() {}

    public UserDto selectByUsernamePassword(String username, String password) throws SQLException {
        UserDto user = selectByUsername(username);
        if (user != null && user.password.equals(password)) {
            return user;
        }
        return null;
    }

    public UserDto selectByUsername(String username) throws SQLException {
        String query = "SELECT userId, username, password FROM users WHERE username = ?";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            UserDto user = new UserDto(result);
            return user;
        }

        return null;
    }

    public UserDto insert(UserDto user) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.username);
        statement.setString(2, user.password);
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();

        if (result.next()) {
            int userId = result.getInt(1);
            return new UserDto(userId, user.username, user.password);
        }

        return user;
    }
}
