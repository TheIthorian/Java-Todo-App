package com.todo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ArgumentParserTest {

    @Test
    public void readArgs_returnsEmptyArgumentCollection_whenArgsIsEmpty() {
        // Given
        ArgumentParser argumentParser = new ArgumentParser();
        String[] args = new String[] {};

        // When
        ArgumentCollection result = argumentParser.readArgs(args);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void readArgs_returnsArgumentCollection() {
        // Given
        ArgumentParser argumentParser = new ArgumentParser();
        String[] args = new String[] { "-flag", "username=my_username" };

        // When
        ArgumentCollection result = argumentParser.readArgs(args);

        // Then
        assertEquals("", result.get("-flag"));
        assertTrue(result.contains("-flag"));
        assertEquals("my_username", result.get("username"));
        assertTrue(result.contains("username"));
    }

    @Test
    public void get_returnsNullWhenNoInCollection() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();

        // When / Then
        assertNull(collection.get("any"));
    }

    @Test
    public void addOption_correctlyAddsArgumentOption() {
        // Given
        ArgumentParser parser = new ArgumentParser();

        // When
        parser.addOption("flag", "description");

        // Then
        assertEquals("flag", parser.getOptions().get(0).flag);
        assertEquals("description", parser.getOptions().get(0).description);
    }
}
