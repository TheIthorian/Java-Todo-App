public class ConfigurationController implements IConfigurationController {

    public ConfigurationController() {}

    public void setUsername(String username) {
        if (username != null) {
            System.out.println("Your username is: " + username);
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            System.out.println("Your password is: " + password);
        }
    }
}
