package com.todo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ArgumentParserTest {

    @Test
    public void returnsEmptyArgumentCollection_whenArgsIsEmpty() {
        // Given
        ArgumentParser argumentParser = new ArgumentParser();
        String[] args = new String[] {};

        // When
        ArgumentCollection result = argumentParser.readArgs(args);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void returnsArgumentCollection() {
        // Given
        ArgumentParser argumentParser = new ArgumentParser();
        String[] args = new String[] { "-flag", "username=my_username" };

        System.out.println(args[0]);

        // When
        ArgumentCollection result = argumentParser.readArgs(args);

        // Then
        assertEquals("", result.get("-flag"));
        assertTrue(result.contains("-flag"));
        assertEquals("my_username", result.get("username"));
        assertTrue(result.contains("username"));
    }

    @Test
    public void ArgumentCollection_get_returnsNullWhenNoInCollection() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();

        // When / Then
        assertNull(collection.get("any"));
    }
}
