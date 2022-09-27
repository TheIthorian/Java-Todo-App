package com.todo;

import java.util.Scanner;
import org.json.JSONObject;

public interface IResourceHandler {
    public boolean exists(String pathname);

    public void create(String pathname);

    public void writeText(String pathname, String data);

    public void writeJSON(String pathname, JSONObject data);

    public Scanner readText(String pathname);

    public JSONObject readJSON(String pathname);

}
