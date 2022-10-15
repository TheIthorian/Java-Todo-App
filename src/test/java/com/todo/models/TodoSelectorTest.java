package com.todo.models;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import com.todo.TestDatabase;
import com.todo.models.TodoSelector.TodoDto;

public class TodoSelectorTest {

    private static final User mockUser = Mockito.mock(User.class);
    private static int USER_ID = 99;

    private static TestDatabase database = new TestDatabase("temp.db");

    static final TodoModel todo1 = new TodoModel("todo 1", "desc 1", mockUser);
    static final TodoModel todo2 = new TodoModel("todo 2", "desc 2", mockUser);

    private static void insertExistingItems() throws SQLException {
        mockUser.userId = USER_ID;

        database.refresh();
        Connection conn = database.connect();
        database.dml(
                "INSERT INTO Users (userId, username, password) values (99, 'username', 'password')",
                conn);
        database.dml("INSERT INTO todo (title, description, userId) VALUES ('" + todo1.title
                + "', '" + todo1.description + "', " + USER_ID + ")", conn);
        database.dml("INSERT INTO todo (title, description, userId) VALUES ('" + todo2.title
                + "', '" + todo2.description + "', " + USER_ID + ")", conn);
        conn.close();
    }

    private static ResultSet selectById(int id, Connection conn) throws SQLException {
        return database.select(
                "SELECT id, title, description, userId from Todo WHERE id = " + id + "", conn);
    }

    @BeforeClass
    public static void setupDatabase() throws SQLException {
        database.createTables();
        insertExistingItems();
    }

    @AfterClass
    public static void cleanUp() {
        database.destroy();
    }

    @Test
    public void selectAll_returnsAllTodoItems() throws SQLException {
        // Given
        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        List<TodoDto> todoItems = selector.selectAll();
        selector.disconnect();

        // Then
        assertEquals(2, todoItems.size());
        assertEquals(todo1.title, todoItems.get(0).title);
        assertEquals(todo1.description, todoItems.get(0).description);
        assertEquals(todo1.userId, todoItems.get(0).userId);
        assertEquals(todo2.title, todoItems.get(1).title);
    }

    @Test
    public void selectAll_returnsEmptyTodoItems_whenNoneExist() throws SQLException {
        // Given
        cleanUp();
        setupDatabase();

        User mockUser = Mockito.mock(User.class);
        mockUser.userId = 1; // Make sure no items exist for the user
        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        List<TodoDto> todoItems = selector.selectAll();
        selector.disconnect();

        // Then
        assertEquals(0, todoItems.size());
    }

    @Test
    public void selectByTitle_returnsCorrectItems() throws SQLException {
        // Given
        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        List<TodoDto> todoItems = selector.selectByTitle(todo1.title);
        selector.disconnect();

        // Then
        assertEquals(1, todoItems.size());
        assertEquals(todo1.title, todoItems.get(0).title);
        assertEquals(todo1.description, todoItems.get(0).description);
        assertEquals(todo1.userId, todoItems.get(0).userId);
    }

    @Test
    public void insert_correctlyInsertsTodoItem() throws SQLException {
        // Given
        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        TodoDto input = new TodoModel("todo 3", "desc 3", mockUser);
        selector.insert(input);
        selector.disconnect();

        Connection conn = database.connect();
        ResultSet newTodo = selectById(3, conn);

        // Then
        assertEquals(3, newTodo.getInt("id"));
        assertEquals("todo 3", newTodo.getString("title"));
        assertEquals("desc 3", newTodo.getString("description"));
        assertEquals(USER_ID, newTodo.getInt("userId"));
        conn.close();
    }

    @Test
    public void update_correctlyUpdatesTodoItem() throws SQLException {
        // Given
        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        TodoDto input = new TodoDto(2, "todo 2 - updated", "desc 2 - updated", USER_ID);
        selector.update(input);
        selector.disconnect();

        Connection conn = database.connect();
        ResultSet newTodo = selectById(2, conn);

        // Then
        assertEquals(2, newTodo.getInt("id"));
        assertEquals("todo 2 - updated", newTodo.getString("title"));
        assertEquals("desc 2 - updated", newTodo.getString("description"));
        assertEquals(USER_ID, newTodo.getInt("userId"));
        conn.close();
    }
}
