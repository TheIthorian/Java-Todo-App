package com.todo;

/*
 * Class to parse execution arguments.
 */
public class ArgumentParser {
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

}
