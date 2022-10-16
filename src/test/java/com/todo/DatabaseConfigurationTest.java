package com.todo;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DatabaseConfigurationTest {
    @Test
    public void InMemory_createdCorrectconfiguration() {
        // Given
        DatabaseConfiguration configuration = DatabaseConfiguration.InMemory();

        // When / Then
        assertEquals(configuration.databaseLocation, "");
        assertEquals(configuration.databaseFileName, ":memory:");
    }
}
