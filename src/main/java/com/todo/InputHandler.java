package com.todo;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputHandler implements IInputHandler {
    public InputHandler() {}

    public String awaitInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String inputValue = "";

        try {
            System.out.println(prompt);
            inputValue = scanner.nextLine();
        } catch (IllegalStateException | NoSuchElementException e) {
            System.out.println("System.in was closed; exiting");
        } finally {
            scanner.close();
        }

        return inputValue;
    }

    public String awaitInput() {
        return awaitInput("");
    }
}
