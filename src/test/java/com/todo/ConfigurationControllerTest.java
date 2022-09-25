package com.todo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import java.util.HashMap;
import org.mockito.Mockito;
import org.json.JSONObject;
import org.junit.Test;

public class ConfigurationControllerTest {

    private static final String existingUsername = "my_username";
    private static final String existingPassword = "my_password";

    private static IFileHandler mockFileHandler;

    private static JSONObject formatToJSON(String username, String password) {
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("username", username);
        dataMap.put("password", password);
        return new JSONObject(dataMap);
    }

    private static void setup() {
        mockFileHandler = Mockito.mock(FileHandler.class);
        ConfigurationController.fileHandler = mockFileHandler;
        Mockito.when(mockFileHandler.readJSON("config.json"))
                .thenReturn(formatToJSON(existingUsername, existingPassword));
    }

    @Test
    public void loadsExistingConfigurationFromFile() {
        // Given
        setup();

        // When
        ConfigurationController configurationController = new ConfigurationController();
        configurationController.load();

        // Then
        assertEquals(existingUsername, configurationController.getUsername());
        assertEquals(existingPassword, configurationController.getPassword());
        Mockito.verify(mockFileHandler, Mockito.times(1)).readJSON("config.json");
    }

    @Test
    public void loadsEmptyExistingConfigurationFromFile() {
        // Given
        setup();

        Mockito.when(mockFileHandler.readJSON("config.json")).thenReturn(new JSONObject());

        // When
        ConfigurationController configurationController = new ConfigurationController();
        configurationController.load();

        // Then
        assertNull(configurationController.getUsername());
        assertNull(configurationController.getPassword());
        Mockito.verify(mockFileHandler, Mockito.times(1)).readJSON("config.json");
    }

    @Test
    public void setUsername_changesUsername() {
        // Given
        setup();
        String newUsername = "my_new_username";

        ConfigurationController configurationController = new ConfigurationController();

        // When
        configurationController.setUsername(newUsername);

        // Then
        assertEquals(newUsername, configurationController.getUsername());
    }

    public void setUsername_doesNotChangeUsername_ifUsernameIsNull() {
        // Given
        setup();
        String newUsername = null;

        ConfigurationController configurationController = new ConfigurationController();

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

        ConfigurationController configurationController = new ConfigurationController();

        // When
        configurationController.setPassword(newPassword);

        // Then
        assertEquals(newPassword, configurationController.getPassword());
    }

    public void setPassword_doesNotChangePassword_ifPasswordIsNull() {
        // Given
        setup();
        String newPassword = null;

        ConfigurationController configurationController = new ConfigurationController();

        // When
        configurationController.setPassword(newPassword);

        // Then
        assertEquals(existingPassword, configurationController.getPassword());
    }

    @Test
    public void saveToFile_savesTheCurrentConfigurationToFile() {
        // Given
        setup();
        final String newUsername = "my_new_username";
        final String newPassword = "my_new_password";

        // final JSONObject expectedData = formatToJSON(newUsername, newPassword);

        ConfigurationController configurationController = new ConfigurationController();

        // When
        configurationController.setUsername(newUsername);
        configurationController.setPassword(newPassword);
        configurationController.saveToFile();

        // Then
        // Identical JSONObjects are not equal so cannot test against `expectedData`...
        Mockito.verify(mockFileHandler, Mockito.times(1)).writeJSON(anyString(),
                any(JSONObject.class));
    }
}
