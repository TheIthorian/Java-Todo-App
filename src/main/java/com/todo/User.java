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

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.userId;
    }

    public static User makeUser(String username, String password) throws SecurityException {
        User user = null;
        try {
            if (isPasswordCorrect(username, password)) {
                user = getByUsername(username);
            } else {
                throw new SecurityException("Password not valid");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Database.RecordNotFoundException e) {
            throw new SecurityException("Password not valid");
        }

        return user;
    }

    public static boolean isPasswordCorrect(String username, String password) throws SQLException {
        String query = "SELECT username, password FROM users WHERE username ? AND password = ?";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        conn.close();
        return result.next();
    }

    private static User getByUsername(String username)
            throws SQLException, Database.RecordNotFoundException {
        String query = "SELECT userId, username, password FROM Users WHERE username = ?";

        Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        conn.close();

        if (result.next()) {
            return new User(result.getInt("userId"), result.getString("username"),
                    result.getString("password"));
        } else {
            throw new Database.RecordNotFoundException();
        }
    }

    public static void createTable(Connection conn) throws SQLException {
        String createUsers = "CREATE TABLE IF NOT EXISTS users "
                + "(userId integer PRIMARY KEY, username text NOT NULL, password text NOT NULL)";

        Statement statement = conn.createStatement();
        statement.execute(createUsers);
        statement.close();
        conn.commit();
    }

}
