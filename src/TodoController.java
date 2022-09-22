public class TodoController {
    public IArgumentParser argumentParser = new ArgumentParser();
    public IInputHandler inputHandler = new InputHandler();
    public ITodoService todoService = new TodoService();
    public IConfigurationController configurationController = new ConfigurationController();

    public TodoController() {}

    public void run(String[] args) {
        System.out.println("Running TodoController");

        ArgumentCollection arguments = argumentParser.readArgs(args);

        this.handleConfigurationOperation(arguments);
        this.handleTodoOperation(arguments);

        this.inputHandler.awaitInput("Prees enter to close...");
    }

    private void handleConfigurationOperation(ArgumentCollection arguments) {
        if (arguments.contains("-cf")) {
            this.configurationController.setUsername(arguments.get("username"));
            this.configurationController.setPassword(arguments.get("password"));
        }
    }

    private void handleTodoOperation(ArgumentCollection arguments) {
        if (arguments.contains("-a")) {
            this.todoService.addTodo(arguments.get("title"), arguments.get("description"));
        } else if (arguments.contains("-u")) {
            this.todoService.updateTodo(arguments.get("title"), arguments.get("newTitle"),
                    arguments.get("description"));
        } else if (arguments.contains("-d")) {
            this.todoService.deleteTodo(arguments.get("title"));
        }
    }
}
