package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.exception.MethodNotFoundException;
import com.relogiclabs.jschema.internal.library.CommonMethods;
import com.relogiclabs.jschema.internal.library.MethodEvaluator;

public interface EValue extends Scriptable {
    EValue VOID = new EValue() {
        @Override
        public EType getType() {
            return EType.VOID;
        }

        @Override
        public boolean toBoolean() {
            return false;
        }

        @Override
        public String toString() {
            return EType.VOID.toString();
        }
    };

    default EType getType() {
        return EType.ANY;
    }

    default boolean toBoolean() {
        return true;
    }

    @Override
    default MethodEvaluator getMethod(String name, int argCount) {
        try {
            return CommonMethods.getInstance().getMethod(name, argCount);
        } catch(MethodNotFoundException e) {
            e.setType(getType());
            throw e;
        }
    }
}