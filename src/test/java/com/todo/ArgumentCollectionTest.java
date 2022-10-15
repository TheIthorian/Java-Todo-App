package com.todo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ArgumentCollectionTest {
    @Test
    public void contains_returnsTrue_whenKeyExists() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();
        collection.put("key", "value");

        // When / Then
        assertTrue(collection.contains("key"));
    }

    @Test
    public void contains_returnsFalse_whenKeyDoesNotExist() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();

        // When / Then
        assertFalse(collection.contains("key"));
    }

    @Test
    public void contains_returnsTrue_whenItemInKeyListExists() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();
        collection.put("key2", "value");

        List<String> keys = new ArrayList<String>();
        keys.add("key1");
        keys.add("key2");

        // When / Then
        assertTrue(collection.contains(keys));
    }

    @Test
    public void contains_returnsFalse_whenItemNotInKeyList() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();
        collection.put("key3", "value");

        List<String> keys = new ArrayList<String>();
        keys.add("key1");
        keys.add("key2");

        // When / Then
        assertFalse(collection.contains(keys));
    }

    @Test
    public void isEmpty_returnsTrue_whenCollectionIsEmpty() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();

        // When / Then
        assertTrue(collection.isEmpty());
    }

    @Test
    public void isEmpty_returnFalse_whenCollectionIsNotEmpty() {
        // Given
        ArgumentCollection collection = new ArgumentCollection();
        collection.put("key", "value");

        // When / Then
        assertFalse(collection.isEmpty());
    }
}
