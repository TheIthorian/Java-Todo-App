public interface ITodoService {
    public void addTodo(String title, String description);

    public void updateTodo(String title, String newTitle, String description);

    public void deleteTodo(String title);
}
