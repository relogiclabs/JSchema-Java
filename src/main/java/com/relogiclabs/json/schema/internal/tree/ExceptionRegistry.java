package com.relogiclabs.json.schema.internal.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class ExceptionRegistry implements Iterable<Exception> {
    private int disableException;

    @Getter private final Queue<Exception> exceptions;
    @Getter @Setter private boolean throwException;
    @Getter @Setter private int cutoffLimit = 200;

    public ExceptionRegistry(boolean throwException) {
        this.throwException = throwException;
        this.exceptions = new LinkedList<>();
    }

    public void tryAdd(Exception exception) {
        if(disableException == 0 && exceptions.size() < cutoffLimit)
            exceptions.add(exception);
    }

    public void tryThrow(RuntimeException exception) {
        if(throwException && disableException == 0) throw exception;
    }

    public <T> T tryExecute(Supplier<T> function) {
        try {
            disableException += 1;
            return function.get();
        } finally {
            disableException -= 1;
        }
    }

    @Override
    public Iterator<Exception> iterator() {
        return exceptions.iterator();
    }

    public void clear() {
        exceptions.clear();
    }
}