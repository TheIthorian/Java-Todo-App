package com.todo;

import java.util.Scanner;
import org.json.JSONObject;

/**
 * Interface to interact with persistent resources.
 */
public interface IResourceHandler {
    /**
     * Returns `true` if a file exists with the given `pathname`.
     */
    public boolean exists(String pathname);

    /*
     * Creates a new file at the given `pathname` location. Will not overwrite existing files.
     */
    public void create(String pathname);

    /**
     * Writes the input text `data` to the specified `pathname` file location.
     */
    public void writeText(String pathname, String data);

    /**
     * Writes the input json `data` to the specified `pathname` file location.
     */
    public void writeJSON(String pathname, JSONObject data);

    /**
     * Returns a `Scanner` object of the file at the given `pathname` location.
     */
    public Scanner readText(String pathname);

    /**
     * Returns a `JSONObject` object containing the json data of the file at the given `pathname`
     * location.
     */
    public JSONObject readJSON(String pathname);

}
