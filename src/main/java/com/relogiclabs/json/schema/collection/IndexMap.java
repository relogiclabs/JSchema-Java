package com.relogiclabs.json.schema.collection;

import java.util.Collection;
import java.util.Set;

public interface IndexMap<TK, TV extends Keyable<TK>> extends Collection<TV> {
    TV get(int index);
    TV get(TK key);
    Set<TK> keySet();
    Collection<TV> values();
    boolean isReadOnly();
    void makeReadOnly();
}
