package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.InvalidSelfStateException;
import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.internal.engine.ScriptTreeHelper;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EValue;

import java.util.Collections;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.message.ErrorCode.ARREMT01;
import static com.relogiclabs.jschema.message.ErrorCode.ARRRON01;
import static com.relogiclabs.jschema.message.ErrorCode.NUMARR01;
import static com.relogiclabs.jschema.type.EType.ARRAY;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;

public class ArrayMethods1 extends AbstractMethods {
    @ScriptMethod(ARRAY)
    public EInteger length() {
        return GInteger.from(getSelf(EArray.class).size());
    }

    @ScriptMethod(ARRAY)
    public EInteger push(EValue element, EValue... elements) {
        var array = getSelf(EArray.class);
        requireWritableArray(array);
        array.set(array.size(), element);
        for(var v : elements) array.set(array.size(), v);
        return GInteger.from(array.size());
    }

    @ScriptMethod(ARRAY)
    public EValue pop() {
        var array = getSelf(EArray.class);
        requireWritableArray(array);
        requireNonEmptyArray(array);
        return dereference(array.elements().remove(array.size() - 1));
    }

    @ScriptMethod(ARRAY)
    public EValue find(EValue element) {
        var array = getSelf(EArray.class);
        for(var i = 0; i < array.size(); i++)
            if(areEqual(array.get(i), element, getRuntime())) return GInteger.from(i);
        return UNDEFINED;
    }

    @ScriptMethod(ARRAY)
    public EValue find(EValue element, EInteger start) {
        var array = getSelf(EArray.class);
        for(var i = (int) start.getValue(); i < array.size(); i++)
            if(areEqual(array.get(i), element, getRuntime())) return GInteger.from(i);
        return UNDEFINED;
    }

    @ScriptMethod(ARRAY)
    public EArray fill(EValue value, EInteger length) {
        return GArray.filledFrom(value, (int) length.getValue());
    }

    @ScriptMethod(ARRAY)
    public EArray sort() {
        var array = getSelf(EArray.class);
        requireWritableArray(array);
        if(isAllNumbers(array)) array.elements().sort(comparingDouble(e -> toDouble(dereference(e))));
        else array.elements().sort(comparing(e -> stringify(dereference(e))));
        return array;
    }

    private static boolean isAllNumbers(EArray array) {
        for(var e : array.elements())
            if(!(dereference(e) instanceof ENumber)) return false;
        return true;
    }

    @ScriptMethod(ARRAY)
    public EArray reverse() {
        var array = getSelf(EArray.class);
        requireWritableArray(array);
        Collections.reverse(array.elements());
        return array;
    }

    @ScriptMethod(ARRAY)
    public EValue max() {
        var array = getSelf(EArray.class);
        requireNonEmptyArray(array);
        return array.elements().stream().map(ScriptTreeHelper::dereference)
            .max(comparingDouble(this::toDouble))
            .orElseThrow(() -> new IllegalStateException("Array max not found"));
    }

    @ScriptMethod(ARRAY)
    public EValue min() {
        var array = getSelf(EArray.class);
        requireNonEmptyArray(array);
        return array.elements().stream().map(ScriptTreeHelper::dereference)
            .min(comparingDouble(this::toDouble))
            .orElseThrow(() -> new IllegalStateException("Array min not found"));
    }

    @ScriptMethod(ARRAY)
    public EValue sum() {
        var sum = getSelf(EArray.class).elements().stream()
            .map(ScriptTreeHelper::dereference)
            .mapToDouble(this::toDouble).sum();
        if(sum % 1 == 0) return GInteger.from((long) sum);
        else return GDouble.from(sum);
    }

    @ScriptMethod(ARRAY)
    public EArray copy() {
        return new GArray(getSelf(EArray.class).elements());
    }

    private void requireNonEmptyArray(EArray array) {
        if(array.size() != 0) return;
        var exception =  new InvalidSelfStateException(ARREMT01, "Array cannot be empty");
        exception.setSelf(getSelf());
        exception.setMethod(getMethod());
        throw exception;
    }

    private void requireWritableArray(EArray array) {
        if(!array.isReadonly()) return;
        var exception = new InvalidSelfStateException(ARRRON01, "Array cannot be readonly");
        exception.setSelf(getSelf());
        exception.setMethod(getMethod());
        throw exception;
    }

    private double toDouble(EValue value) {
        if(value instanceof ENumber n) return n.toDouble();
        var exception = new InvalidSelfStateException(NUMARR01,
            "All array elements must be numerical");
        exception.setSelf(getSelf());
        exception.setMethod(getMethod());
        throw exception;
    }
}