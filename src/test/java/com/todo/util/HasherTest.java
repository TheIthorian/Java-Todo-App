package com.todo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HasherTest {

    private static String INPUT = "my password";
    private static String SALT = "$2a$10$JbBto608jRsom8rCFP/mpu";
    private static String HASH = "$2a$10$JbBto608jRsom8rCFP/mpubiC8PectkmdwHW/TUrr0FUsEn3BL82a";

    @Test
    public void salt_returnsString() {
        assertEquals(29, new Hasher().salt().length());
    }

    @Test
    public void hash_correctlyReturnsHashedInput() {
        // Given
        Hasher hasher = new Hasher();

        // When / Then
        assertEquals(HASH, hasher.hash(INPUT, SALT));
    }

    @Test
    public void matches_correctlyReturnsTrue() {
        // Given
        Hasher hasher = new Hasher();

        // When / Then
        assertTrue(hasher.matches(INPUT, HASH));
    }

    @Test
    public void matches_correctlyReturnsFalse() {
        // Given
        Hasher hasher = new Hasher();

        // When / Then
        assertFalse(hasher.matches("wrong password", HASH));
    }
}
