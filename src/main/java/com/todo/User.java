package com.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private int userId;
    public String username;
    public String password;
    public boolean isAuthenticated = false;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.userId;
    }

    public boolean isPasswordCorrect() throws SQLException {
        return isPasswordCorrect(this.username, this.password);
    }

    public static boolean isPasswordCorrect(String username, String password) throws SQLException {
        String query = "SELECT username, password FROM users WHERE username = ? AND password = ?";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        conn.close();
        return result.next();
    }

    public static User getByUsernamePassword(String username, String password) throws SQLException {
        String query =
                "SELECT userId, username, password FROM Users WHERE username = ? AND password = ?";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();

        result.next();
        final User user = new User(result.getInt("userId"), result.getString("username"),
                result.getString("password"));

        conn.close();

        return user;

    }

    public void insert() throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.username);
        statement.setString(2, this.password);
        statement.executeUpdate();

        ResultSet result = statement.getGeneratedKeys();
        result.next();
        this.userId = result.getInt(1);
        conn.close();

        this.isAuthenticated = true;
    }

    public static boolean usernameExists(String username) {
        try {
            String query = "SELECT 1 FROM Users WHERE username = ?";

            Connection conn = Database.connect();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.executeQuery();
            ResultSet result = statement.getGeneratedKeys();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static void createTable(Connection conn) throws SQLException {
        String createUsers = "CREATE TABLE IF NOT EXISTS users "
                + "(userId integer PRIMARY KEY, username text NOT NULL, password text NOT NULL)";

        Statement statement = conn.createStatement();
        statement.execute(createUsers);
        statement.close();
    }

}
