package com.relogiclabs.jschema.internal.engine;

public interface ScriptOperation {
    String ADDITION = "addition";
    String SUBTRACTION = "subtraction";
    String MULTIPLICATION = "multiplication";
    String DIVISION = "division";
    String INDEX = "index";
    String RANGE = "range";
    String COMPARISON = "comparison";
    String PROPERTY = "property access";
    String INCREMENT = "increment";
    String DECREMENT = "decrement";
    String NEGATION = "negation";
}