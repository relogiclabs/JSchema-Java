package com.relogiclabs.jschema.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
// To use this, interface ScriptFunctions must be implemented
public @interface ScriptFunction {
    String[] value() default {};
}