package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.exception.AnnotationAttributeException;

import static com.relogiclabs.jschema.message.ErrorCode.ATRALS01;
import static com.relogiclabs.jschema.message.ErrorCode.ATRALS02;

public class AnnotationHelper {
    @SafeVarargs
    public static <T> T[] nonEmptyFrom(T[] values, T... defaultValues) {
        return values == null || values.length == 0 ? defaultValues : values;
    }

    @SafeVarargs
    public static <T> T[] processAliases(T[]... values) {
        return processAliases(false, values);
    }

    @SafeVarargs
    public static <T> T[] processAliases(boolean optional, T[]... values) {
        T[] nonEmptyValue = null;
        for(var value : values) {
            if(isNonEmpty(value)) {
                if(nonEmptyValue != null) throw new AnnotationAttributeException(ATRALS01,
                    "More than one aliases for an attribute cannot be set");
                nonEmptyValue = value;
            }
        }
        if(!optional && nonEmptyValue == null) throw new AnnotationAttributeException(ATRALS02,
            "None of the aliases for the mandatory attribute is set");
        return nonEmptyValue;
    }

    private static <T> boolean isNonEmpty(T[] value) {
        return value != null && value.length > 0;
    }
}