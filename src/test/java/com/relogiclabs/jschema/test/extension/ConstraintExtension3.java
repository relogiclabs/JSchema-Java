package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.node.JNumber;

// Functions for negative (error) test cases
public class ConstraintExtension3 {
    // To use @ConstraintFunction, interface ConstraintFunctions must be implemented
    // ConstraintExtension and GeneralExtension are available built-in implementations
    @ConstraintFunction
    public boolean odd(JNumber target) {
        // Precision loss is not considered here
        if(target.toDouble() % 2 != 0) return true;
        throw new RuntimeException("Not an odd number");
    }
}