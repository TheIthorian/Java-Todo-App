package com.todo;

import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import com.todo.controllers.ConfigurationController;

public class ConfigurationValidatorTest {

    private static ConfigurationController mockConfigurationController =
            Mockito.mock(ConfigurationController.class);

    private static String databaseLocation = "databaseLocation";
    private static String username = "username";
    private static String password = "password";

    @BeforeClass
    public static void setup() {
        Mockito.when(mockConfigurationController.getDatabaseLocation()).thenReturn(databaseLocation);
        Mockito.when(mockConfigurationController.getUsername()).thenReturn(username);
        Mockito.when(mockConfigurationController.getPassword()).thenReturn(password);
    }

    @Test
    public void isValid_returnsTrue_whenCorrectConfigurationIsProvided() {
        // Given
        setup();
        ConfigurationValidator validator = new ConfigurationValidator();

        // When / Then
        assertTrue(validator.isValid(mockConfigurationController));
    }
}
