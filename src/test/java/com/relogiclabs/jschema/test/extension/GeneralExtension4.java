package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.GeneralExtension;
import com.relogiclabs.jschema.node.JNumber;

// Functions for negative (error) test cases
public class GeneralExtension4 extends GeneralExtension {
    @ConstraintFunction
    public boolean odd(JNumber target) {
        // Any RuntimeException can be thrown without details
        return fail(new RuntimeException("something went wrong"));
    }
}