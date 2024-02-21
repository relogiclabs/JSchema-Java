package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.ScriptCommonException;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.message.ErrorCode.INDX01;

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
        for(var v : collection) elements.add(new GReference(v));
    }

    public static GArray filledFrom(EValue value, int size) {
        var array = new GArray(size);
        for(int i = 0; i < size; i++) array.elements.add(new GReference(value));
        return array;
    }

    public static GArray from(EArray array, GRange range) {
        var list = array.elements();
        var size = list.size();
        return new GArray(list.subList(range.getStart(size), range.getEnd(size)));
    }

    @Override
    public EValue get(int index) {
        var size = elements.size();
        if(index < size) return elements.get(index);
        if(index > MAX_LIMIT) throw new ScriptCommonException(INDX01,
                concat("Array index ", index, " exceeds maximum size limit"));
        if(index > size) throw new ArrayIndexOutOfBoundsException(index);
        var element = new GReference(VOID);
        elements.add(element);
        return element;
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