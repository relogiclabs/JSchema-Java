package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.DuplicateDefinitionException;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.engine.ScriptContext;
import com.relogiclabs.jschema.message.MessageFormatter;
import com.relogiclabs.jschema.node.JAlias;
import com.relogiclabs.jschema.node.JDefinition;
import com.relogiclabs.jschema.node.JValidator;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.DEFI01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter
public final class RuntimeContext {
    private final FunctionRegistry functions;
    private final PragmaRegistry pragmas;
    private final Map<JAlias, JValidator> definitions;
    private final ExceptionRegistry exceptions;
    private final Map<String, FutureFunction> futures;
    private final ReceiverRegistry receivers;
    private final Map<String, Object> storage;
    private final MessageFormatter messageFormatter;
    private final ScriptContext scriptContext;

    public RuntimeContext(MessageFormatter messageFormatter, boolean throwException) {
        this.messageFormatter = messageFormatter;
        this.definitions = new HashMap<>();
        this.functions = new FunctionRegistry(this);
        this.pragmas = new PragmaRegistry();
        this.exceptions = new ExceptionRegistry(throwException);
        this.receivers = new ReceiverRegistry();
        this.storage = new HashMap<>();
        this.futures = new HashMap<>();
        this.scriptContext = new ScriptContext(this);
    }

    public JDefinition addDefinition(JDefinition definition) {
        var previous = definitions.get(definition.getAlias());
        if(previous != null) throw new DuplicateDefinitionException(formatForSchema(DEFI01,
                concat("Duplicate definition of '", definition.getAlias(),
                        "' is found and already defined as ", previous.getOutline()),
                definition.getContext()));
        definitions.put(definition.getAlias(), definition.getValidator());
        return definition;
    }

    public boolean areEqual(double value1, double value2) {
        return Math.abs(value1 - value2) < pragmas.getFloatingPointTolerance();
    }

    public boolean addFuture(FutureFunction future) {
        return futures.put(UUID.randomUUID().toString(), future) == null;
    }

    public boolean invokeFutures() {
        var result = true;
        for(var f : futures.values()) result &= f.invoke();
        return result;
    }

    public void clear() {
        exceptions.clear();
        storage.clear();
        receivers.clear();
    }
}