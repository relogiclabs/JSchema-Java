package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.ScriptCommonException;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.ITER01;
import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public final class GIterator implements Iterable<EValue> {
    private final EValue iterable;

    public static GIterator of(EValue iterable) {
        return new GIterator(dereference(iterable));
    }

    @Override
    public Iterator<EValue> iterator() {
        if(iterable instanceof EArray array) return iterator(array);
        if(iterable instanceof EObject object) return iterator(object);
        if(iterable instanceof EString string) return iterator(string);
        else throw new ScriptCommonException(ITER01,
                concat("Invalid type ", iterable.getType(), " for iteration"));
    }

    public Iterator<EValue> iterator(EArray array) {
        while(array.size() == 1) {
            if(array.get(0) instanceof EArray)
                array = (EArray) array.get(0);
            else break;
        }
        var iterator = array.elements().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public EValue next() {
                return iterator.next();
            }
        };
    }

    public Iterator<EValue> iterator(EString string) {
        String value = string.getValue();
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < value.length();
            }

            @Override
            public EValue next() {
                return GString.of(value.charAt(index++));
            }
        };
    }

    public Iterator<EValue> iterator(EObject object) {
        var iterator = object.keySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public EValue next() {
                return GString.of(iterator.next());
            }
        };
    }
}