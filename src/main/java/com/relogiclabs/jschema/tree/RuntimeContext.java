package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.DuplicateAliasException;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.engine.ScriptGlobalScope;
import com.relogiclabs.jschema.internal.tree.ConstraintRegistry;
import com.relogiclabs.jschema.internal.tree.ScriptRegistry;
import com.relogiclabs.jschema.message.MessageFormatter;
import com.relogiclabs.jschema.message.OutlineFormatter;
import com.relogiclabs.jschema.node.JAlias;
import com.relogiclabs.jschema.node.JDefinition;
import com.relogiclabs.jschema.node.JValidator;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.jschema.message.ErrorCode.ALSDUP01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter
public final class RuntimeContext {
    private final Map<JAlias, JValidator> definitions = new HashMap<>();
    private final List<FutureFunction> futures = new LinkedList<>();
    private final ReceiverRegistry receivers = new ReceiverRegistry();
    private final Map<String, Object> storage = new HashMap<>();
    private final OutlineFormatter outlineFormatter = new OutlineFormatter();

    private final ImportRegistry imports;
    private final PragmaRegistry pragmas;
    private final ScriptGlobalScope scriptGlobalScope;
    private final ExceptionRegistry exceptions;
    private final MessageFormatter messageFormatter;

    public RuntimeContext(MessageFormatter messageFormatter, boolean throwException) {
        this.messageFormatter = messageFormatter;
        this.exceptions = new ExceptionRegistry(throwException);
        this.imports = new ImportRegistry(this);
        this.pragmas = new PragmaRegistry(this);
        this.scriptGlobalScope = new ScriptGlobalScope(this);
    }

    public JDefinition addDefinition(JDefinition definition) {
        var previous = definitions.get(definition.getAlias());
        if(previous != null) throw new DuplicateAliasException(formatForSchema(ALSDUP01,
            "Duplicate definition of '" + definition.getAlias() + "' is found and already defined as "
                + previous.getOutline(), definition.getContext()));
        definitions.put(definition.getAlias(), definition.getValidator());
        return definition;
    }

    public ConstraintRegistry getConstraints() {
        return imports.getConstraints();
    }

    public ScriptRegistry getScripts() {
        return imports.getScripts();
    }

    public boolean areEqual(double value1, double value2) {
        return Math.abs(value1 - value2) < pragmas.getFloatingPointTolerance();
    }

    public int compare(double value1, double value2) {
        if(areEqual(value1, value2)) return 0;
        return Double.compare(value1, value2);
    }

    public boolean addFuture(FutureFunction future) {
        return futures.add(future);
    }

    public boolean invokeFutures() {
        var result = true;
        for(var f : futures) result &= f.invoke();
        return result;
    }

    public void clear() {
        exceptions.clear();
        storage.clear();
        receivers.clear();
    }
}