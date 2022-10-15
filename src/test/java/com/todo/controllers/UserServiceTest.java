package com.todo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.sql.Connection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import com.todo.Database;
import com.todo.models.UserSelector;
import com.todo.models.UserSelector.UserDto;
import com.todo.models.User;

public class UserServiceTest {
    private static Database mockDatabase;

    private static UserSelector mockSelector;

    private void setup() throws Exception {
        mockDatabase = Mockito.mock(Database.class);

        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(mockDatabase.connect()).thenReturn(connection);

        mockSelector = Mockito.mock(UserSelector.class);
    }

    private UserSelector.UserDto makeDto(String username, String password) {
        final UserSelector.UserDto queryResult = new UserSelector.UserDto();
        queryResult.username = username;
        queryResult.password = password;
        return queryResult;
    }

    @Test
    public void getUser_successfullyReturnsUser() throws Exception {
        // Given
        setup();
        final String username = "My user username";
        final String password = "My user password";

        final UserSelector.UserDto queryResult = makeDto(username, password);

        Mockito.when(mockSelector.selectByUsernamePassword(username, password)).thenReturn(queryResult);

        // When
        UserService userService = new UserService(mockDatabase);
        userService.selector = mockSelector;
        User result = userService.getUser(username, password);

        // Then
        assertEquals(username, result.username);
        assertEquals(password, result.password);
        verify(mockSelector, times(1)).disconnect();
    }

    @Test
    public void getUser_returnsNull_whenNoUserIsFound() throws Exception {
        // Given
        setup();
        final String username = "My user username";
        final String password = "My user password";

        Mockito.when(mockSelector.selectByUsernamePassword(anyString(), anyString())).thenReturn(null);

        // When
        UserService userService = new UserService(mockDatabase);
        userService.selector = mockSelector;
        User result = userService.getUser(username, password);

        // Then
        assertNull(result);
        verify(mockSelector, times(1)).disconnect();
    }

    @Test
    public void addUser_successfullyAddsUser() throws Exception {
        // Given
        setup();
        final String username = "My user username";
        final String password = "My user password";
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // When
        UserService userService = new UserService(mockDatabase);
        userService.selector = mockSelector;
        userService.addUser(username, password);

        // Then
        Mockito.verify(mockSelector, Mockito.times(1)).insert(userCaptor.capture());

        User user = userCaptor.getValue();
        assertEquals(username, user.username);
        assertEquals(password, user.password);
        verify(mockSelector, times(1)).disconnect();
    }

    @Test
    public void addUser_throwsErrorWhenUserAlreadyExists() throws Exception {
        // Given
        setup();
        final String username = "My user username";
        final String password = "My user username";
        final UserDto existingUser = makeDto(username, password);

        Mockito.when(mockSelector.selectByUsername(anyString())).thenReturn(existingUser);

        // When / Then
        UserService userService = new UserService(mockDatabase);
        userService.selector = mockSelector;
        try {
            userService.addUser(username, password);
            fail("UsernameAlreadyExists exception not thrown");
        } catch (UserService.UserValidationErrors.UsernameAlreadyExists e) {
            
        }
        verify(mockSelector, times(1)).disconnect();
    }
}
