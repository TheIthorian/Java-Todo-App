package com.todo.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSelector {

    private Connection conn;

    public static class UserDto {
        protected int userId;
        String username;
        String password;

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
    }

    UserSelector(Connection conn) {
        this.conn = conn;
    }

    UserDto selectByUsernamePassword(String username, String password) {
        UserDto user = selectByUsername(username);
        if (user.password == password) {
            return user;
        }
        return null;
    }

    UserDto selectByUsername(String username) {
        String query = "SELECT userId, username, password FROM users WHERE username = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                UserDto user = new UserDto(result);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    UserDto insert(UserDto user) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try {
            PreparedStatement statement =
                    conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.username);
            statement.setString(2, user.password);
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                int userId = result.getInt(1);
                return new UserDto(userId, user.username, user.password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
