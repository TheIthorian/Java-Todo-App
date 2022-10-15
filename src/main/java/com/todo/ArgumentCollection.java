package com.todo;

import java.util.HashMap;
import java.util.List;

/**
 * Class to manage a collection of execution arguments.
 */
public class ArgumentCollection {
    private HashMap<String, String> argumentMap = new HashMap<String, String>();

    public ArgumentCollection() {}

    /**
     * Returns `true` if the collection contains the given `key`.
     */
    public boolean contains(String key) {
        return this.argumentMap.containsKey(key);
    }

    /**
     * Returns `true` if the collection contains any one of the input `keys`.
     */
    public boolean contains(List<String> keys) {
        for (String key : keys) {
            if (this.argumentMap.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an argument to the collection with the given `key` and `value`.
     */
    public void put(String key, String value) {
        this.argumentMap.put(key, value);
    }

    /**
     * Returns the stored value for the given argument `key`.
     */
    public String get(String key) {
        return this.argumentMap.get(key);
    }

    /**
     * Returns `true` if the argument collection is empty.
     */
    public boolean isEmpty() {
        return this.argumentMap.isEmpty();
    }
}
