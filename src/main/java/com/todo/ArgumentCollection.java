package com.todo;

import java.util.HashMap;
import java.util.List;

public class ArgumentCollection {
    private HashMap<String, String> argumentMap = new HashMap<String, String>();

    public ArgumentCollection() {}

    public boolean contains(String key) {
        return this.argumentMap.containsKey(key);
    }

    public boolean contains(List<String> keys) {
        for (String key : keys) {
            if (this.argumentMap.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public void put(String key, String value) {
        this.argumentMap.put(key, value);
    }

    public String get(String key) {
        return this.argumentMap.get(key);
    }

    public boolean isEmpty() {
        return this.argumentMap.isEmpty();
    }
}
