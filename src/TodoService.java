public class TodoService implements ITodoService {
    public void addTodo(String title, String description) {
        System.out.println("addTodo: " + title);
        System.out.println("\t" + description);
    }

    public void updateTodo(String title, String newTitle, String description) {
        System.out.println("updateTodo: " + title);
        System.out.println("\t" + newTitle);
        System.out.println("\t" + description);
    }

    public void deleteTodo(String title) {
        System.out.println("delete: " + title);
    }

}
