package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.ScriptIteratorException;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;

import static com.relogiclabs.jschema.message.ErrorCode.ITERSE01;

@Getter
@RequiredArgsConstructor
public final class GIterator implements Iterable<EValue> {
    private final EValue iterable;

    @Override
    public Iterator<EValue> iterator() {
        if(iterable instanceof EArray array) return iterator(array);
        if(iterable instanceof EObject object) return iterator(object);
        if(iterable instanceof EString string) return iterator(string);
        throw new ScriptIteratorException(ITERSE01, "Invalid type "
            + iterable.getType() + " for iteration");
    }

    @SuppressWarnings("unchecked")
    private Iterator<EValue> iterator(EArray array) {
        return (Iterator<EValue>) array.elements().iterator();
    }

    private Iterator<EValue> iterator(EString string) {
        return string.getValue().codePoints().mapToObj(c
            -> (EValue) GString.from(c)).iterator();
    }

    private Iterator<EValue> iterator(EObject object) {
        return object.keySet().stream().map(k
            -> (EValue) GString.from(k)).iterator();
    }
}