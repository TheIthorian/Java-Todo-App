package com.todo.models;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.mockito.Mockito;

import com.todo.Database;

public class BaseSelectorTest {

    private Database mockDatabase;
    private Connection mockConnection;

    public class TestSelector extends BaseSelector {}

    private void setup() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockDatabase = Mockito.mock(Database.class);
        Mockito.when(mockDatabase.connect()).thenReturn(mockConnection);
    }

    @Test
    public void connect_setsTheConnectionProperty() throws SQLException {
        // Given
        setup();
        TestSelector selector = new TestSelector();

        // When
        selector.connect(mockDatabase);

        // Then
        assertEquals(mockConnection, selector.conn);
    }

    @Test
    public void disconnect_closesTheConnection() throws SQLException {
        // Given
        setup();
        TestSelector selector = new TestSelector();
        selector.conn = mockConnection;

        // When
        selector.disconnect();

        // Then
        Mockito.verify(mockConnection, Mockito.times(1)).close();
        assertNull(selector.conn);
    }
}
