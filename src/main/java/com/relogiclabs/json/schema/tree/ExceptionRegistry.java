package com.relogiclabs.json.schema.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class ExceptionRegistry implements Iterable<Exception> {
    private int disableException;

    private final Queue<Exception> exceptions;
    @Getter private final Queue<Exception> tryBuffer;
    @Getter @Setter private boolean throwException;
    @Getter @Setter private int cutoffLimit = 500;

    public ExceptionRegistry(boolean throwException) {
        this.throwException = throwException;
        this.exceptions = new LinkedList<>();
        this.tryBuffer = new LinkedList<>();
    }

    private boolean addException(Queue<Exception> queue, Exception exception) {
        if(queue.size() <= cutoffLimit) queue.add(exception);
        return false;
    }

    public boolean failWith(RuntimeException exception) {
        exception.fillInStackTrace();
        if(disableException > 0) return addException(tryBuffer, exception);
        if(throwException) throw exception;
        return addException(exceptions, exception);
    }

    public <T> T tryExecute(Supplier<T> function) {
        try {
            disableException += 1;
            return function.get();
        } finally {
            if(disableException >= 1) disableException -= 1;
            else throw new IllegalStateException("Invalid runtime state");
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