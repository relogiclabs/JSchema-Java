package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.ScriptCommonException;
import com.relogiclabs.jschema.exception.UpdateNotSupportedException;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.message.ErrorCode.AUPD03;
import static com.relogiclabs.jschema.message.ErrorCode.AWRT01;
import static com.relogiclabs.jschema.message.ErrorCode.AWRT02;

@Getter
@EqualsAndHashCode
public final class GArray implements EArray {
    private static final int MAX_LIMIT = Short.MAX_VALUE;
    private final List<EValue> elements;

    private GArray(int size) {
        this.elements = new ArrayList<>(size);
    }

    public GArray(Collection<? extends EValue> collection) {
        this.elements = new ArrayList<>(collection.size());
        for(var v : collection) elements.add(new GLeftValue(v));
    }

    public static GArray filledFrom(EValue value, int size) {
        var array = new GArray(size);
        for(int i = 0; i < size; i++) array.elements.add(new GLeftValue(value));
        return array;
    }

    public static GArray from(EArray array, GRange range) {
        var list = array.elements();
        var size = list.size();
        return new GArray(list.subList(range.getStart(size), range.getEnd(size)));
    }

    @Override
    public EValue get(int index) {
        return elements.get(index);
    }

    @Override
    public void set(int index, EValue value) {
        var size = elements.size();
        if(index > MAX_LIMIT) throw new ScriptCommonException(AWRT02,
            "Array index " + index + " exceeds maximum size limit");
        if(index == size) elements.add(new GLeftValue(value));
        else if(index < size) {
            if(!(elements.get(index) instanceof GLeftValue l))
                throw new UpdateNotSupportedException(AUPD03,
                    "Readonly array index " + index + " cannot be updated");
            l.setValue(value);
        } else throw new ScriptCommonException(AWRT01,
            "Index " + index + " is out of bounds for writing to array length " + size);
    }

    @Override
    public List<? extends EValue> elements() {
        return elements;
    }

    @Override
    public String toString() {
        return joinWith(elements, ", ", "[", "]");
    }
}