package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.type.EType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
// To use this, interface ScriptMethods must be implemented
public @interface ScriptMethod {
    EType[] value() default {}; // alias of types
    String[] names() default {};
    EType[] types() default {};
}