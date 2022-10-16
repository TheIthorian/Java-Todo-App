package com.todo.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.todo.TestDatabase;
import com.todo.models.UserSelector.UserDto;

public class UserSelectorTest {

    private static TestDatabase database = new TestDatabase("temp.db");

    static final User user1 = new User("username 1", "password 1");
    static final User user2 = new User("username 2", "password 2");

    private static void insertExistingUsers() throws SQLException {
        database.refresh();
        Connection conn = database.connect();
        database.dml("INSERT INTO users (username, password) VALUES ('" + user1.username + "', '"
                + user1.password + "')", conn);
        user1.userId = 1;

        database.dml("INSERT INTO users (username, password) VALUES ('" + user2.username + "', '"
                + user2.password + "')", conn);
        user2.userId = 2;

        conn.close();
    }

    private static ResultSet selectById(int id, Connection conn) throws SQLException {
        return database.select(
                "SELECT userId, username, password from users WHERE userId = " + id + "", conn);
    }

    @BeforeClass
    public static void setupDatabase() throws SQLException {
        database.createTables();
        insertExistingUsers();
    }

    @AfterClass
    public static void cleanUp() {
        database.destroy();
    }

    @Test
    public void selectByUsernamePassword_returnsUserWithSameUsernameAndPassword()
            throws SQLException {
        // Given
        UserSelector selector = new UserSelector();
        selector.connect(database);

        // When
        UserDto user = selector.selectByUsernamePassword(user1.username, user1.password);
        selector.disconnect();

        // Then
        assertEquals(user1.username, user.username);
        assertEquals(user1.password, user.password);
        assertEquals(user1.userId, user.userId);
    }

    @Test
    public void selectByUsernamePassword_returnsNull_whenPasswordDoesNotMatch()
            throws SQLException {
        // Given
        UserSelector selector = new UserSelector();
        selector.connect(database);

        // When
        UserDto user = selector.selectByUsernamePassword(user1.username, "incorrect password");
        selector.disconnect();

        // Then
        assertNull(user);
    }

    @Test
    public void selectByUsernamePassword_returnsNull_whenUsernameDoesNotExist()
            throws SQLException {
        // Given
        UserSelector selector = new UserSelector();
        selector.connect(database);

        // When
        UserDto user = selector.selectByUsernamePassword("Some unknown username", user1.password);
        selector.disconnect();

        // Then
        assertNull(user);
    }

    @Test
    public void selectByUsername_returnsUserWithSameUsername() throws SQLException {
        // Given
        cleanUp();
        setupDatabase();

        // Given
        UserSelector selector = new UserSelector();
        selector.connect(database);

        // When
        UserDto user = selector.selectByUsername(user1.username);
        selector.disconnect();

        // Then
        assertEquals(user1.username, user.username);
        assertEquals(user1.password, user.password);
        assertEquals(user1.userId, user.userId);
    }

    @Test
    public void selectByUsername_returnsNull_whenUsernameDoesNotExist() throws SQLException {
        // Given
        UserSelector selector = new UserSelector();
        selector.connect(database);

        // When
        UserDto user = selector.selectByUsername("Some unknown username");
        selector.disconnect();

        // Then
        assertNull(user);
    }

    @Test
    public void insert_correctlyInsertsUser() throws SQLException {
        // Given
        UserSelector selector = new UserSelector();
        selector.connect(database);

        // When
        UserDto input = new User("username 3", "password 3");
        selector.insert(input);
        selector.disconnect();

        Connection conn = database.connect();
        ResultSet newUser = selectById(3, conn);

        // Then
        assertEquals(3, newUser.getInt("userId"));
        assertEquals("username 3", newUser.getString("username"));
        assertEquals("password 3", newUser.getString("password"));
        conn.close();
    }
}
