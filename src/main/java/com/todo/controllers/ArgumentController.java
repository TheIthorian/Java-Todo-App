package com.todo.controllers;

import com.todo.ArgumentParser;

public class ArgumentController {
    public static void addOptions(ArgumentParser parser) {
        parser.addOption("-cf",
                "Update local configuration. Expects (optional) username, (optional) password, (optional) db inputs.");
        parser.addOption("-addUser",
                "Adds a new user. Uses the username and password stored in config.json");
        parser.addOption("-a",
                "Adds a new todo item. Expects title and (optional) description inputs.");
        parser.addOption("-u",
                "Updates a todo item with the matching input title. Expects title, (optional) newTitle, (optional) description inputs.");
        parser.addOption("-g",
                "Gets all todo items, or any todo items matching the input title if one is provided. Expects (optional) title input");
    }
}
