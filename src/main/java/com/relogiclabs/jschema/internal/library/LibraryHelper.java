package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.ArgumentTypeException;
import com.relogiclabs.jschema.exception.ArgumentValueException;
import com.relogiclabs.jschema.exception.InvalidArgumentException;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.message.OutlineFormatter.createOutline;

public class LibraryHelper {
    private LibraryHelper() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    static int asInteger(EValue value) {
        return (int) asLong(value);
    }

    static long asLong(EValue value) {
        if(!(value instanceof EInteger i)) throw failOnInvalidArgumentType(value);
        return i.getValue();
    }

    static double asDouble(EValue value) {
        if(!(value instanceof ENumber n)) throw failOnInvalidArgumentType(value);
        return n.toDouble();
    }

    static String asString(EValue value) {
        if(!(value instanceof EString s)) throw failOnInvalidArgumentType(value);
        return s.getValue();
    }

    static <T> T cast(EValue value, Class<T> type) {
        if(!type.isInstance(value)) throw failOnInvalidArgumentType(value);
        return type.cast(value);
    }

    static <T> T castMember(EObject object, String key, Class<T> type) {
        var value = dereference(object.get(key));
        if(!type.isInstance(value)) throw failOnInvalidArgumentValue(object);
        return type.cast(value);
    }

    private static InvalidArgumentException failOnInvalidArgumentType(EValue argument) {
        return new ArgumentTypeException("Invalid argument type " + argument.getType());
    }

    private static InvalidArgumentException failOnInvalidArgumentValue(EValue argument) {
        return new ArgumentValueException("Invalid argument value " + createOutline(argument));
    }
}