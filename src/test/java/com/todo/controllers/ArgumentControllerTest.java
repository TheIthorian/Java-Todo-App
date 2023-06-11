package com.todo.controllers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.todo.ArgumentParser;

public class ArgumentControllerTest {
    @Test
    public void addOptions_addsCorrectOptions() {
        // Given
        ArgumentParser parser = new ArgumentParser();

        // When
        ArgumentController.addOptions(parser);

        // Then
        assertEquals(5, parser.getOptions().size());
    }

}
