package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class GControl implements EValue {
    private static final int BREAK_FLAG = 1;
    private static final int RETURN_FLAG = 2;
    public static final GControl BREAK = new GControl(BREAK_FLAG, VOID);

    private final int flag;
    @Getter private final EValue value;

    public static GControl ofReturn(EValue value) {
        return new GControl(RETURN_FLAG, value);
    }

    public boolean isBreak() {
        return flag == BREAK_FLAG;
    }

    public boolean isReturn() {
        return flag == RETURN_FLAG;
    }

    public EValue toIteration() {
        return flag == BREAK_FLAG ? VOID : this;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}