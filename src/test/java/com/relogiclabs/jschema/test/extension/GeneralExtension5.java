package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.GeneralExtension;
import com.relogiclabs.jschema.node.JNumber;

import java.io.FileNotFoundException;

// Functions for negative (error) test cases
public class GeneralExtension5 extends GeneralExtension {
    @ConstraintFunction
    public boolean odd(JNumber target) throws FileNotFoundException {
        // Avoid checked exception where possible
        throw new FileNotFoundException("file not found");
    }
}