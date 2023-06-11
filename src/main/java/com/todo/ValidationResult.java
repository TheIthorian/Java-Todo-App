package com.todo;

import java.util.ArrayList;

public class ValidationResult {
    public boolean isValid = false;
    public ArrayList<String> errors = new ArrayList<String>();

    public static ValidationResult success() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.isValid = true;
        return validationResult;
    }

    public static ValidationResult error() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.isValid = true;
        return validationResult;
    }

    public ValidationResult addError(String errorText) {
        this.isValid = false;
        this.errors.add(errorText);
        return this;
    }

}
