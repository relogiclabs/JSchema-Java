package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;

// Functions for negative (error) test cases
public class ExternalFunctions3 extends FunctionProvider {
    public boolean odd() {
        throw new RuntimeException("Error occurred");
    }
}