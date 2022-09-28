package com.todo.controllers;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import com.todo.Database;
import com.todo.models.TodoModel;
import com.todo.models.TodoSelector;
import com.todo.models.User;

public class TodoServiceTest {

    private Database mockDatabase;
    private User user;
    MockedConstruction<TodoSelector> mockSelectors;

    private void setup() throws Exception {
        mockDatabase = Mockito.mock(Database.class);
        user = Mockito.mock(User.class);

        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(mockDatabase.connect()).thenReturn(connection);

        mockSelectors = Mockito.mockConstruction(TodoSelector.class);
    }

    @Test
    public void addTodo_successfullyAddsTodo() throws Exception {
        // Given
        setup();
        final String title = "My todo title";
        final String description = "My todo description";
        final ArgumentCaptor<TodoModel> todoCaptor = ArgumentCaptor.forClass(TodoModel.class);

        // When
        TodoService todoService = new TodoService(mockDatabase);
        todoService.addTodo(title, description, user);

        // Then
        TodoSelector mockSelector = mockSelectors.constructed().get(0);
        Mockito.verify(mockSelector, Mockito.times(1)).insert(todoCaptor.capture());

        TodoModel todo = todoCaptor.getValue();
        assertEquals(title, todo.title);
        assertEquals(description, todo.description);
        assertEquals(0, todo.userId); // Default value
    }
}
