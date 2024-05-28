package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;

// Functions for negative (error) test cases
public class ExternalFunctions5 extends FunctionProvider {
    public ExternalFunctions5() {
        throw new RuntimeException("Error occurred");
    }
}