package com.todo.controllers;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import com.todo.Database;
import com.todo.models.TodoModel;
import com.todo.models.TodoSelector;
import com.todo.models.User;

public class TodoServiceTest {

    private Database mockDatabase;
    private User user;
    private final int USER_ID = 99;
    TodoSelector mockSelector;

    private void setup() throws Exception {
        mockDatabase = Mockito.mock(Database.class);
        user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(USER_ID);

        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(mockDatabase.connect()).thenReturn(connection);

        mockSelector = Mockito.mock(TodoSelector.class);
    }

    private TodoSelector.TodoDto makeDto(String title, String description) {
        final TodoSelector.TodoDto queryResult = new TodoSelector.TodoDto();
        queryResult.title = title;
        queryResult.description = description;
        queryResult.userId = user.getId();
        return queryResult;
    }

    @Test
    public void getByTitle_successfullyReturnsTodo() throws Exception {
        // Given
        setup();
        final String title = "My todo title";
        final String description = "My todo title";

        final List<TodoSelector.TodoDto> queryResults = new ArrayList<TodoSelector.TodoDto>();
        final TodoSelector.TodoDto queryResult = makeDto(title, description);
        queryResults.add(queryResult);

        Mockito.when(mockSelector.selectByTitle(title)).thenReturn(queryResults);

        // When
        TodoService todoService = new TodoService(mockDatabase, user);
        todoService.selector = mockSelector;
        TodoModel result = todoService.getByTitle(title).get(0);

        // Then
        assertEquals(title, result.title);
        assertEquals(description, result.description);
        assertEquals(USER_ID, result.userId);
    }

    @Test
    public void addTodo_successfullyAddsTodo() throws Exception {
        // Given
        setup();
        final String title = "My todo title";
        final String description = "My todo description";
        final ArgumentCaptor<TodoModel> todoCaptor = ArgumentCaptor.forClass(TodoModel.class);

        // When
        TodoService todoService = new TodoService(mockDatabase, user);
        todoService.selector = mockSelector;
        todoService.addTodo(title, description);

        // Then
        Mockito.verify(mockSelector, Mockito.times(1)).insert(todoCaptor.capture());

        TodoModel todo = todoCaptor.getValue();
        assertEquals(title, todo.title);
        assertEquals(description, todo.description);
        assertEquals(USER_ID, todo.userId);
    }

    @Test
    public void updateTodo_successfullyUpdatesTodo() throws Exception {
        // Given
        setup();
        final String title = "My todo title";
        final String description = "My todo title";

        final String newTitle = "My new todo title";
        final String newDescription = "My new description";
        final ArgumentCaptor<TodoModel> todoCaptor = ArgumentCaptor.forClass(TodoModel.class);

        final List<TodoSelector.TodoDto> queryResults = new ArrayList<TodoSelector.TodoDto>();
        final TodoSelector.TodoDto queryResult = makeDto(title, description);
        queryResults.add(queryResult);
        Mockito.when(mockSelector.selectByTitle(title)).thenReturn(queryResults);

        // When
        TodoService todoService = new TodoService(mockDatabase, user);
        todoService.selector = mockSelector;
        todoService.updateTodo(title, newTitle, newDescription);

        // Then
        Mockito.verify(mockSelector, Mockito.times(1)).update(todoCaptor.capture());

        TodoModel todo = todoCaptor.getValue();
        assertEquals(newTitle, todo.title);
        assertEquals(newDescription, todo.description);
        assertEquals(USER_ID, todo.userId);
    }
}
