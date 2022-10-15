package com.todo.models;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import com.todo.TestDatabase;
import com.todo.models.TodoSelector.TodoDto;

public class TodoSelectorTest {

    private User mockUser = Mockito.mock(User.class);
    private int USER_ID = 99;

    private TestDatabase database;

    private List<TodoModel> existingTodoItems = new ArrayList<TodoModel>();

    private void setupDatabase() {
        database = new TestDatabase("temp.db");
        database.createTables();
    }

    private void insertExistingItems() throws SQLException {
        mockUser.userId = USER_ID;
        TodoModel todo1 = new TodoModel("todo 1", "desc 1", mockUser);
        TodoModel todo2 = new TodoModel("todo 2", "desc 2", mockUser);
        existingTodoItems.add(todo1);
        existingTodoItems.add(todo2);

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

    @Test
    public void selectAll_returnsAllTodoItems() throws SQLException {
        // Given
        setupDatabase();
        insertExistingItems();

        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        List<TodoDto> todoItems = selector.selectAll();

        // Then
        assertEquals(2, todoItems.size());
        assertEquals(existingTodoItems.get(0).title, todoItems.get(0).title);
        assertEquals(existingTodoItems.get(0).description, todoItems.get(0).description);
        assertEquals(existingTodoItems.get(0).userId, todoItems.get(0).userId);
        assertEquals(existingTodoItems.get(1).title, todoItems.get(1).title);
    }

    @Test
    public void selectAll_returnsEmptyTodoItems_whenNoneExist() throws SQLException {
        // Given
        setupDatabase();

        TodoSelector selector = new TodoSelector(mockUser);
        selector.connect(database);

        // When
        List<TodoDto> todoItems = selector.selectAll();

        // Then
        assertEquals(0, todoItems.size());
    }
}
