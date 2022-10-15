package com.todo.models;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import com.todo.Database;
import com.todo.DatabaseConfiguration;
import com.todo.IResourceHandler;
import com.todo.models.TodoSelector.TodoDto;

public class TodoSelectorTest {

    private User mockUser = Mockito.mock(User.class);
    private int USER_ID = 99;

    private DatabaseConfiguration databaseConfiguration =
            new DatabaseConfiguration("./assets/", "temp.db");
    private IResourceHandler mockResourceHandler = Mockito.mock(IResourceHandler.class);
    private Database database;

    private List<TodoModel> existingTodoItems = new ArrayList<TodoModel>();

    private void setupDatabase() {
        mockUser.userId = USER_ID;
        
        Mockito.when(mockResourceHandler.exists(anyString())).thenReturn(false);
        database = new Database(databaseConfiguration, mockResourceHandler);
        database.createTables();
    }

    private void insertExistingItems() throws SQLException {
        TodoModel todo1 = new TodoModel("todo 1", "desc 1", mockUser);
        TodoModel todo2 = new TodoModel("todo 2", "desc 2", mockUser);
        existingTodoItems.add(todo1);
        existingTodoItems.add(todo2);

        Connection conn = database.connect();
        runDML("DELETE FROM todo", conn);
        runDML("DELETE FROM users", conn);
        runDML("INSERT INTO Users (userId, username, password) values (99, 'username', 'password')",
                conn);
        runDML("INSERT INTO todo (title, description, userId) VALUES ('todo 1', 'desc 1', 99)",
                conn);
        runDML("INSERT INTO todo (title, description, userId) VALUES ('todo 2', 'desc 2', 99)",
                conn);
        conn.close();
    }

    private void runDML(String query, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
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
}
