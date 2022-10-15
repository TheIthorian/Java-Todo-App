package com.todo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
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
        mockConfigurationController.configurationFilePath = "config.json";
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

    @Test
    public void isValid_returnsFalse_whenConfigIsMissingDatabaseLocation() {
        // Given
        setup();
        Mockito.when(mockConfigurationController.getDatabaseLocation()).thenReturn(null);
        ConfigurationValidator validator = new ConfigurationValidator();

        // When / Then
        assertFalse(validator.isValid(mockConfigurationController));
    }

    @Test
    public void isValid_returnsFalse_whenConfigIsMissingUsername() {
        // Given
        setup();
        Mockito.when(mockConfigurationController.getUsername()).thenReturn(null);
        ConfigurationValidator validator = new ConfigurationValidator();

        // When / Then
        assertFalse(validator.isValid(mockConfigurationController));
    }

    @Test
    public void isValid_returnsFalse_whenConfigIsMissingPassword() {
        // Given
        setup();
        Mockito.when(mockConfigurationController.getPassword()).thenReturn(null);
        ConfigurationValidator validator = new ConfigurationValidator();

        // When / Then
        assertFalse(validator.isValid(mockConfigurationController));
    }

    @Test 
    public void getErrors_returnsAllErrors() {
        // Given
        Mockito.when(mockConfigurationController.getDatabaseLocation()).thenReturn(null);
        Mockito.when(mockConfigurationController.getUsername()).thenReturn(null);
        Mockito.when(mockConfigurationController.getPassword()).thenReturn(null);
        ConfigurationValidator validator = new ConfigurationValidator();

        final Map<String, String> expectedErrors = new HashMap<String, String>();
        expectedErrors.put("databaseLocation", "databaseLocation is not specified in config.json");
        expectedErrors.put("username", "username is not specified in config.json");
        expectedErrors.put("password", "password is not specified in config.json");

        // When 
        validator.isValid(mockConfigurationController);
        Map<String, String> errors = validator.getErrors();
        
        // Then
        assertEquals(expectedErrors, errors);
    }
}
