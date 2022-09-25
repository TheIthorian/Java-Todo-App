package com.todo;

public class TodoController {
    public static IArgumentParser argumentParser = new ArgumentParser();
    public static IInputHandler inputHandler = new InputHandler();
    public static ITodoService todoService = new TodoService();
    public static IConfigurationController configurationController = new ConfigurationController();

    public TodoController() {}

    public void run(String[] args) {
        ArgumentCollection arguments = argumentParser.readArgs(args);

        this.handleConfigurationOperation(arguments);
        this.handleTodoOperation(arguments);

        this.handleNoOperation(arguments);

        inputHandler.awaitInput("Prees enter to close...");
    }

    private void handleConfigurationOperation(ArgumentCollection arguments) {
        if (arguments.contains("-cf")) {
            configurationController.load();
            configurationController.setUsername(arguments.get("username"));
            configurationController.setPassword(arguments.get("password"));
            configurationController.saveToFile();
        }
    }

    private void handleTodoOperation(ArgumentCollection arguments) {
        if (arguments.contains("-a")) {
            todoService.addTodo(arguments.get("title"), arguments.get("description"));
        } else if (arguments.contains("-u")) {
            todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"));
        } else if (arguments.contains("-d")) {
            todoService.deleteTodo(arguments.get("title"));
        }
    }

    private void handleNoOperation(ArgumentCollection arguments) {
        if (!arguments.isEmpty()) {
            return;
        }

        // Start the application.
        // Walk through adding, updating etc

        /*
         * Select option: Find / All / Add / Update / Remove
         */
    }
}
