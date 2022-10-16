package com.todo.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Mockito;
import com.todo.Database;
import com.todo.controllers.UserService;
import com.todo.models.User;

public class UserAuthenticatorTest {

    private Database mockDatabase;
    private UserService mockUserService;
    private User user;

    private void setup() {
        mockDatabase = Mockito.mock(Database.class);
        mockUserService = Mockito.mock(UserService.class);
        user = Mockito.mock(User.class);
    }

    @Test
    public void authenticate_returnsTrue() {
        // Given
        setup();
        final UserAuthenticator userAuthenticator = new UserAuthenticator(mockDatabase);

        // When
        boolean result = userAuthenticator.authenticate(user);

        // Then
        assertTrue(result);
    }

    @Test
    public void getUser_returnsUser() {
        // Given
        setup();
        String username = "username";
        String password = "password";
        final User user = new User(username, password);

        Mockito.when(mockUserService.getUser(username, password)).thenReturn(user);

        final UserAuthenticator userAuthenticator = new UserAuthenticator(mockDatabase);
        userAuthenticator.userService = mockUserService;

        // When
        User result = userAuthenticator.getUser(username, password);

        // Then
        assertEquals(username, result.username);
        assertEquals(password, result.password);
    }

    @Test
    public void getUser_returnsNull_whenNoUserFound() {
        // Given
        setup();
        String username = "username";
        String password = "password";

        Mockito.when(mockUserService.getUser(username, password)).thenReturn(null);

        final UserAuthenticator userAuthenticator = new UserAuthenticator(mockDatabase);
        userAuthenticator.userService = mockUserService;

        // When
        User result = userAuthenticator.getUser(username, password);

        // Then
        assertNull(result);
    }
}
