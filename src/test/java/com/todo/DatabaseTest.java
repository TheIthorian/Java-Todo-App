package com.todo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.mockito.Mockito;

public class DatabaseTest {

    private static IResourceHandler resourceHandler = Mockito.mock(FileHandler.class);
    private static DatabaseConfiguration configuration = DatabaseConfiguration.InMemory();

    @Test
    public void alreadyExists_returnsFalse() {
        // Given
        Database database = new Database(configuration, resourceHandler);
        Mockito.when(
            resourceHandler.exists(configuration.databaseLocation + configuration.databaseFileName)
        ).thenReturn(false);

        // When / Then
        assertFalse(database.alreadyExists());
    }

    @Test
    public void alreadyExists_returnTrue() {
        // Given
        Database database = new Database(configuration, resourceHandler);
        Mockito.when(
            resourceHandler.exists(configuration.databaseLocation + configuration.databaseFileName)
        ).thenReturn(true);

        // When / Then
        assertTrue(database.alreadyExists());
    }

    @Test
    public void connect_returnsDatabaseConnection() throws SQLException {
        // Given
        Database database = new Database(configuration, resourceHandler);
        Mockito.when(
            resourceHandler.exists(configuration.databaseLocation + configuration.databaseFileName)
        ).thenReturn(false);

        // When 
        Connection conn = database.connect();

        // Then
        assertTrue(conn.isValid(0));
        conn.close();
    }
}
