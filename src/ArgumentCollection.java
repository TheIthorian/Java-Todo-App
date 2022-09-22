import java.util.HashMap;

public class ArgumentCollection {
    private HashMap<String, String> argumentMap = new HashMap<String, String>();

    public ArgumentCollection() {}

    public boolean contains(String key) {
        return this.argumentMap.containsKey(key);
    }

    public void put(String key, String value) {
        this.argumentMap.put(key, value);
    }

    public String get(String key) {
        return this.argumentMap.get(key);
    }
}
