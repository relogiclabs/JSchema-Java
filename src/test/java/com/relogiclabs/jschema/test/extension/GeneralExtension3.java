package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.GeneralExtension;

// Functions for negative (error) test cases
public class GeneralExtension3 extends GeneralExtension {
    @ConstraintFunction
    public boolean odd() {
        // Not register due to invalid signature
        // Missing mandatory target parameter
        return false;
    }
}