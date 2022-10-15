package com.todo.models;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import org.mockito.Mockito;

public class TodoModelTest {

    private int USER_ID = 99;
    private User mockUser;

    private void setup() {
        mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getId()).thenReturn(USER_ID);

    }

    @Test
    public void constructor_correctlySetsMemberVariables() {
        // Given
        setup();
        final String title = "my todo title";
        final String description = "my todo description";

        // When
        TodoModel model = new TodoModel(title, description, mockUser);

        // Then
        assertEquals(model.title, title);
        assertEquals(model.description, description);
        assertEquals(model.userId, USER_ID);
    }

    @Test 
    public void createTable_correctlyCreatesDatabaseTable() throws SQLException {
        // Given
        Connection mockConn = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);
        Mockito.when(mockConn.createStatement()).thenReturn(mockStatement);

        // When
        TodoModel.createTable(mockConn);

        // Then
        Mockito.verify(mockStatement, Mockito.times(1)).execute(Mockito.anyString());
        Mockito.verify(mockStatement, Mockito.times(1)).close();
        Mockito.verify(mockConn, Mockito.times(1)).close();
    }
}
