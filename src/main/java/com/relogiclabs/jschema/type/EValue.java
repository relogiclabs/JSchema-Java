package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.internal.library.CommonLibrary;
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
        return CommonLibrary.getInstance().getMethod(name, argCount);
    }
}