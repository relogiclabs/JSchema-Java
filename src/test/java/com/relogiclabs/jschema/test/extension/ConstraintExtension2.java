package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintExtension;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.node.JNumber;

// Functions for negative (error) test cases
public class ConstraintExtension2 extends ConstraintExtension {
    @ConstraintFunction
    public void odd(JNumber target) {
        // Return type of constraint function must not be void
        // Precision loss is not considered here
        if(target.toDouble() % 2 != 0) return;
        throw new RuntimeException("Not an odd number");
    }
}