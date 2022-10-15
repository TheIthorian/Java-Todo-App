package com.todo;

import java.util.ArrayList;
import java.util.List;

/*
 * Class to parse execution arguments.
 */
public class ArgumentParser {
    public class Argument {
        String flag;
        String description;

        public Argument(String flag, String description) {
            this.flag = flag;
            this.description = description;
        }
    }

    private ArrayList<Argument> arguments = new ArrayList<Argument>();

    public ArgumentParser() {}

    /**
     * Reads the list of string arguments, returning an `ArgumentCollection` object.
     */
    public ArgumentCollection readArgs(String[] args) {
        ArgumentCollection argumentCollection = new ArgumentCollection();

        for (String arg : args) {

            String[] argList = arg.split("=");

            String attribute = argList[0];
            String value = argList.length > 1 ? argList[1] : "";

            argumentCollection.put(attribute, value);
        }

        return argumentCollection;
    }

    /**
     * Adds an option to the possible list of arguments.
     */
    public void addOption(String flag, String description) {
        Argument argument = new Argument(flag, description);
        arguments.add(argument);
    }

    /**
     * Returns a list of all allowed options.
     */
    public List<Argument> getOptions() {
        return this.arguments;
    }

    /**
     * Prints all argument options as previously defined.
     */
    public void printOptions() {
        System.out.println("-h\tprints this help message");
        for (Argument argument : arguments) {
            System.out.println(argument.flag + "\t" + argument.description);
        }
    }

}
