package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintExtension;

// Functions for negative (error) test cases
public class ConstraintExtension4 extends ConstraintExtension {
    public ConstraintExtension4() {
        throw new RuntimeException("Error occurred");
    }
}