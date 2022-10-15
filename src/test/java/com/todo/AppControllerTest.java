// package com.todo;

// import org.junit.Test;
// import org.mockito.Mockito;
// import com.todo.controllers.ConfigurationController;

// public class AppControllerTest {
// private static IInputHandler mockInputHandler;
// private static ConfigurationController mockConfigurationController;
// // private static ITodoService mockTodoService;

// private static void setup() {
// mockInputHandler = Mockito.mock(InputHandler.class);
// mockConfigurationController = Mockito.mock(ConfigurationController.class);
// // mockTodoService = Mockito.mock(TodoService.class);

// AppController.inputHandler = mockInputHandler;
// AppController.configurationController = mockConfigurationController;
// // AppController.todoService = mockTodoService;
// }

// @Test
// public void run_setsGivenUserConfiguration() {
// // When
// setup();
// AppController controller = new AppController();
// final String[] args =
// new String[] { "-cf", "username=my_username", "password=my_password" };

// // When
// controller.run(args);

// // Then
// Mockito.verify(mockConfigurationController).load();
// Mockito.verify(mockConfigurationController).setUsername("my_username");
// Mockito.verify(mockConfigurationController).setPassword("my_password");
// Mockito.verify(mockConfigurationController).save();
// }

// @Test
// public void run_doesNotSetConfiguration() {
// // When
// setup();
// AppController controller = new AppController();
// final String[] args = new String[] {};

// // When
// controller.run(args);

// // Then
// Mockito.verify(mockConfigurationController).load();
// Mockito.verify(mockConfigurationController, Mockito.never())
// .setUsername(Mockito.anyString());
// Mockito.verify(mockConfigurationController, Mockito.never())
// .setPassword(Mockito.anyString());
// Mockito.verify(mockConfigurationController, Mockito.never()).save();
// }
// }
