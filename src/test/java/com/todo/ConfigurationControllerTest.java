package com.todo;

import static org.junit.Assert.assertEquals;
import org.json.JSONObject;
import org.junit.Test;

public class ConfigurationControllerTest {

    private static final String existingUsername = "my_username";
    private static final String existingPassword = "my_password";
    private static String existingConfig;

    private static final String testConfigFilename = "testConfig.json";

    private static void setup() {
        ConfigurationController.configurationFilePath = testConfigFilename;
        existingConfig = formatToJSON(existingUsername, existingPassword);
        new FileHandler().writeText(testConfigFilename, existingConfig);
    }

    private static String formatToJSON(String username, String password) {
        return String.format("{\"password\":\"%s\",\"username\":\"%s\"}", password, username);
    }

    @Test
    public void loadsExistingConfigurationFromFile() {
        // Given
        setup();

        // When
        ConfigurationController configurationController = new ConfigurationController();

        // Then
        assertEquals(existingUsername, configurationController.getUsername());
        assertEquals(existingPassword, configurationController.getPassword());
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
        String newUsername = "my_new_username";
        String newPassword = "my_new_password";

        ConfigurationController configurationController = new ConfigurationController();

        // When
        configurationController.setUsername(newUsername);
        configurationController.setPassword(newPassword);
        configurationController.saveToFile();

        // Then
        FileHandler fileHandler = new FileHandler();
        JSONObject savedData = fileHandler.readJSON(testConfigFilename);
        assertEquals(newUsername, savedData.get("username"));
        assertEquals(newPassword, savedData.get("password"));
    }

    // @Test
    // @AfterClass
    // public void deleteTestConfig() {
    // File testConfigFile = new File(testConfigFilename);

    // if (!testConfigFile.delete()) {
    // System.out.println("Failed to delete test configuration file.");
    // }
    // }
}