package com.relogiclabs.json.schema.tree;

import com.relogiclabs.json.schema.exception.DuplicateDefinitionException;
import com.relogiclabs.json.schema.function.FutureValidator;
import com.relogiclabs.json.schema.internal.tree.ExceptionRegistry;
import com.relogiclabs.json.schema.internal.tree.FunctionRegistry;
import com.relogiclabs.json.schema.internal.tree.PragmaRegistry;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.types.JAlias;
import com.relogiclabs.json.schema.types.JDefinition;
import com.relogiclabs.json.schema.types.JFunction;
import com.relogiclabs.json.schema.types.JInclude;
import com.relogiclabs.json.schema.types.JNode;
import com.relogiclabs.json.schema.types.JPragma;
import com.relogiclabs.json.schema.types.JReceiver;
import com.relogiclabs.json.schema.types.JValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI01;


public final class RuntimeContext {
    @Getter private final FunctionRegistry functions;
    @Getter private final PragmaRegistry pragmas;
    @Getter private final Map<JAlias, JValidator> definitions;
    @Getter private final ExceptionRegistry exceptions;
    @Getter private final Map<String, FutureValidator> validators;
    @Getter private final Map<JReceiver, List<JNode>> receivers;
    @Getter private final Map<String, Object> storage;
    @Getter private final MessageFormatter messageFormatter;

    public RuntimeContext(MessageFormatter messageFormatter, boolean throwException) {
        this.messageFormatter = messageFormatter;
        this.definitions = new HashMap<>();
        this.functions = new FunctionRegistry(this);
        this.pragmas = new PragmaRegistry();
        this.exceptions = new ExceptionRegistry(throwException);
        this.receivers = new HashMap<>();
        this.storage = new HashMap<>();
        this.validators = new HashMap<>();
    }

    public JPragma addPragma(JPragma pragma) {
        return pragmas.addPragma(pragma);
    }

    public JInclude addClass(JInclude include) {
        addClass(include.getClassName(), include.getContext());
        return include;
    }

    public void addClass(String className, Context context) {
        functions.addClass(className, context);
    }

    public JDefinition addDefinition(JDefinition definition) {
        var previous = definitions.get(definition.getAlias());
        if(previous != null)
            throw new DuplicateDefinitionException(MessageFormatter.formatForSchema(
                DEFI01, concat("Duplicate definition of ", quote(definition.getAlias()),
                            " is found and already defined as ", previous.getOutline()),
                    definition.getContext()));
        definitions.put(definition.getAlias(), definition.getValidator());
        return definition;
    }

    public boolean invokeFunction(JFunction function, JNode target) {
        return functions.invokeFunction(function, target);
    }

    public boolean areEqual(double value1, double value2) {
        return Math.abs(value1 - value2) < pragmas.getFloatingPointTolerance();
    }

    public <T> T tryExecute(Supplier<T> function) {
        return exceptions.tryExecute(function);
    }

    public void register(List<JReceiver> list) {
        for(var r : list) receivers.put(r, new ArrayList<>());
    }

    public void receive(List<JReceiver> list, JNode node) {
        for(var r : list) receivers.get(r).add(node);
    }

    public List<JNode> fetch(JReceiver receiver) {
        return receivers.get(receiver);
    }

    public boolean addValidator(FutureValidator validator) {
        return validators.put(UUID.randomUUID().toString(), validator) == null;
    }

    public boolean invokeValidators() {
        var result = true;
        for(var v : validators.values()) result &= v.validate();
        return result;
    }

    public boolean failWith(RuntimeException exception) {
        exceptions.tryThrow(exception);
        exceptions.tryAdd(exception);
        return false;
    }

    public void clear() {
        exceptions.clear();
        storage.clear();
        for(var v : receivers.values()) v.clear();
    }
}