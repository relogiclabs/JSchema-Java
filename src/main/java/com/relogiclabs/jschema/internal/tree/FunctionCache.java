package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.MiscellaneousHelper.getDerived;

public class FunctionCache implements Iterable<FunctionCache.Entry> {
    public record Entry(EFunction function, Object[] arguments) {
        public boolean isTargetMatch(JNode target) {
            return function.getTargetType().isInstance(getDerived(target));
        }

        public Object invoke(JFunction caller, JNode target) {
            arguments[0] = getDerived(target);
            return function.invoke(caller, arguments);
        }
    }

    @Getter @Setter
    private static int sizeLimit = 10;
    private final List<Entry> cache;

    public FunctionCache() {
        this.cache = new ArrayList<>(sizeLimit);
    }

    public void add(EFunction function, Object[] arguments) {
        if(cache.size() > sizeLimit) cache.remove(0);
        arguments[0] = null;
        cache.add(new Entry(function, arguments));
    }

    @Override
    public Iterator<Entry> iterator() {
        return cache.iterator();
    }
}