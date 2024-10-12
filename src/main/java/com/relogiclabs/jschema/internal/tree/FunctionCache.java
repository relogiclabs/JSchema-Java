package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.internal.loader.SchemaFunction;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getDerived;

public class FunctionCache implements Iterable<FunctionCache.Entry> {
    public record Entry(SchemaFunction function, Object[] arguments) {
        public boolean isTargetMatched(JNode target) {
            return function.getTargetType().isInstance(getDerived(target));
        }

        public Object invoke(JFunction invoker, JNode target) {
            arguments[0] = getDerived(target);
            return function.invoke(invoker, arguments);
        }
    }

    @Getter @Setter
    private static int sizeLimit = 10;
    private final List<Entry> cache;

    public FunctionCache() {
        this.cache = new LinkedList<>();
    }

    public void add(SchemaFunction function, Object[] arguments) {
        if(cache.size() > sizeLimit) cache.remove(0);
        arguments[0] = null;
        cache.add(new Entry(function, arguments));
    }

    @Override
    public Iterator<Entry> iterator() {
        return cache.iterator();
    }
}