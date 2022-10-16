package com.todo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import java.util.HashMap;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import com.todo.DatabaseConfiguration;
import com.todo.FileHandler;
import com.todo.IResourceHandler;
import org.json.JSONObject;
import org.junit.Test;

public class ConfigurationControllerTest {

    private static final String existingUsername = "my_username";
    private static final String existingPassword = "my_password";
    private static final String existingDatabaseLocation = "my_databaseLocation/";
    private static final String CONFIG_FILE_NAME = "config.json";

    private static IResourceHandler mockResourceHandler;

    private static JSONObject formatToJSON(String username, String password,
            String databaseLocation) {
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("username", username);
        dataMap.put("password", password);
        dataMap.put("databaseLocation", databaseLocation);
        return new JSONObject(dataMap);
    }

    private static void setup() {
        mockResourceHandler = Mockito.mock(IResourceHandler.class);
        Mockito.when(mockResourceHandler.readJSON(CONFIG_FILE_NAME)).thenReturn(
                formatToJSON(existingUsername, existingPassword, existingDatabaseLocation));
    }

    @Test
    public void loadsExistingConfigurationFromFile() {
        // Given
        setup();

        // When
        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // Then
        assertEquals(existingUsername, configurationController.getUsername());
        assertEquals(existingPassword, configurationController.getPassword());
        assertEquals(existingDatabaseLocation, configurationController.getDatabaseLocation());
        Mockito.verify(mockResourceHandler, Mockito.times(1)).readJSON(CONFIG_FILE_NAME);
    }

    @Test
    public void loadsEmptyExistingConfigurationFromFile() {
        // Given
        setup();

        Mockito.when(mockResourceHandler.readJSON(CONFIG_FILE_NAME)).thenReturn(new JSONObject());

        // When
        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // Then
        assertNull(configurationController.getUsername());
        assertNull(configurationController.getPassword());
        assertEquals(ConfigurationController.DEFAULT_DATABASE_LOCATION, configurationController.getDatabaseLocation());
        Mockito.verify(mockResourceHandler, Mockito.times(1)).readJSON(CONFIG_FILE_NAME);
    }

    @Test
    public void createsNewConfiguration_whenConfigurationFileDoesNotExist() {
        // Given
        setup();

        Mockito.when(mockResourceHandler.exists(anyString())).thenReturn(false);
        Mockito.when(mockResourceHandler.readJSON(CONFIG_FILE_NAME)).thenReturn(new JSONObject());

        // When
        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // Then
        Mockito.verify(mockResourceHandler, Mockito.times(1)).create(CONFIG_FILE_NAME);
        Mockito.verify(mockResourceHandler, Mockito.times(1)).writeText(CONFIG_FILE_NAME, "{}");

        assertNull(configurationController.getUsername());
        assertNull(configurationController.getPassword());
        assertEquals(ConfigurationController.DEFAULT_DATABASE_LOCATION, configurationController.getDatabaseLocation());
        Mockito.verify(mockResourceHandler, Mockito.times(1)).readJSON(CONFIG_FILE_NAME);
    }

    @Test
    public void setUsername_changesUsername() {
        // Given
        setup();
        String newUsername = "my_new_username";

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);

        // When
        configurationController.setUsername(newUsername);

        // Then
        assertEquals(newUsername, configurationController.getUsername());
    }

    public void setUsername_doesNotChangeUsername_ifUsernameIsNull() {
        // Given
        setup();
        String newUsername = null;

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // When
        configurationController.setUsername(newUsername);

        // Then
        assertEquals(existingUsername, configurationController.getUsername());
    }

    @Test
    public void setPassword_changesPassword() {
        // Given
        setup();
        String newPassword = "my_new_password";

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);

        // When
        configurationController.setPassword(newPassword);

        // Then
        assertEquals(newPassword, configurationController.getPassword());
    }

    public void setPassword_doesNotChangePassword_ifPasswordIsNull() {
        // Given
        setup();
        String newPassword = null;

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // When
        configurationController.setPassword(newPassword);

        // Then
        assertEquals(existingPassword, configurationController.getPassword());
    }

    @Test
    public void setDatabaseLocation_changesDatabaseLocation() {
        // Given
        setup();
        String newDatabaseLocation = "my_new_databaselocation";

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);

        // When
        configurationController.setDatabaseLocation(newDatabaseLocation);

        // Then
        assertEquals(newDatabaseLocation, configurationController.getDatabaseLocation());
    }

    @Test
    public void setDatabaseLocation_doesNotChangeDatabaseLocation_ifDatabaseLocationIsNull() {
        // Given
        setup();
        String newDatabaseLocation = null;

        mockResourceHandler = Mockito.mock(FileHandler.class);
        Mockito.when(mockResourceHandler.readJSON(CONFIG_FILE_NAME)).thenReturn(
                formatToJSON(existingUsername, existingPassword, existingDatabaseLocation));

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // When
        configurationController.setDatabaseLocation(newDatabaseLocation);

        // Then
        assertEquals(existingDatabaseLocation, configurationController.getDatabaseLocation());
    }

    @Test
    public void getDatabaseConfiguration_returnsCorrectObject() {
        // Given
        setup();
        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.setDatabaseLocation(existingDatabaseLocation);

        // When
        DatabaseConfiguration config = configurationController.getDatabaseConfiguration();

        // Then
        assertEquals(existingDatabaseLocation, config.databaseLocation);
        assertEquals("todo.db", config.databaseFileName);
    }

    @Test
    public void saveToFile_savesTheCurrentConfigurationToFile() {
        // Given
        setup();
        final String newUsername = "my_new_username";
        final String newPassword = "my_new_password";

        ConfigurationController configurationController =
                new ConfigurationController(mockResourceHandler);
        configurationController.load();

        // When
        configurationController.setUsername(newUsername);
        configurationController.setPassword(newPassword);
        configurationController.save();

        // Then
        ArgumentCaptor<JSONObject> dataArgument = ArgumentCaptor.forClass(JSONObject.class);
        Mockito.verify(mockResourceHandler, Mockito.times(1)).writeJSON(anyString(),
                dataArgument.capture());

        JSONObject data = dataArgument.getValue();
        assertEquals(data.get("username"), newUsername);
        assertEquals(data.get("password"), newPassword);
        assertEquals(data.get("databaseLocation"), existingDatabaseLocation);
    }
}
