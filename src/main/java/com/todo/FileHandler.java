package com.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class FileHandler implements IFileHandler {

    public FileHandler() {}

    public static FileHandler newInstance() {
        return new FileHandler();
    }

    public boolean exists(String pathname) {
        File file = new File(pathname);
        return file.exists() && !file.isDirectory();
    }

    public void create(String pathname) {
        try {
            File file = new File(pathname);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred trying to create file: " + pathname);
            e.printStackTrace();
        }
    }

    public void writeText(String pathname, String data) {
        try {
            FileWriter myWriter = new FileWriter(pathname);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred trying to write to file: " + pathname);
            e.printStackTrace();
        }

    }

    public void writeJSON(String pathname, JSONObject data) {
        try (FileWriter file = new FileWriter(pathname)) {
            file.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scanner readText(String pathname) {
        Scanner scanner = null;

        try {
            File file = new File(pathname);
            scanner = new Scanner(file);
            return scanner;
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find text file: " + pathname);
            e.printStackTrace();
        }

        return scanner;
    }

    public JSONObject readJSON(String pathname) {
        JSONObject json = null;

        Scanner scanner = readText(pathname);
        String content = "";

        while (scanner.hasNextLine()) {
            content += scanner.nextLine();
        }

        try {
            json = new JSONObject(content);
        } catch (JSONException e) {
            System.out.println("Unable to parse JSON file: " + pathname);
            e.printStackTrace();
        }

        return json;
    }
}
