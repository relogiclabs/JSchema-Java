package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.types.JFunction;
import com.relogiclabs.json.schema.types.JNode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FunctionCache implements Iterable<FunctionCache.Entry> {

    @Value
    public static class Entry {
        MethodPointer methodPointer;
        Object[] arguments;

        public boolean isTargetMatch(JNode target) {
            return methodPointer.getParameter(0).getType().isInstance(target.getDerived());
        }

        public Object invoke(JFunction function, JNode target) {
            arguments[0] = target.getDerived();
            return methodPointer.invoke(function, arguments);
        }
    }

    @Getter @Setter
    private static int sizeLimit = 10;
    private final List<Entry> cache;

    public FunctionCache() {
        this.cache = new ArrayList<>(sizeLimit);
    }

    public void add(MethodPointer methodPointer, Object[] arguments) {
        if(cache.size() > sizeLimit) cache.remove(0);
        arguments[0] = null;
        cache.add(new Entry(methodPointer, arguments));
    }

    @Override
    public Iterator<Entry> iterator() {
        return cache.iterator();
    }
}