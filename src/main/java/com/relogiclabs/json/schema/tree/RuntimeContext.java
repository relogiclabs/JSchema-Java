package com.relogiclabs.json.schema.tree;

import com.relogiclabs.json.schema.exception.DuplicateDefinitionException;
import com.relogiclabs.json.schema.function.FutureValidator;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.type.JAlias;
import com.relogiclabs.json.schema.type.JDefinition;
import com.relogiclabs.json.schema.type.JValidator;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI01;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;

public final class RuntimeContext {
    @Getter private final FunctionRegistry functions;
    @Getter private final PragmaRegistry pragmas;
    @Getter private final Map<JAlias, JValidator> definitions;
    @Getter private final ExceptionRegistry exceptions;
    @Getter private final Map<String, FutureValidator> validators;
    @Getter private final ReceiverRegistry receivers;
    @Getter private final Map<String, Object> storage;
    @Getter private final MessageFormatter messageFormatter;

    public RuntimeContext(MessageFormatter messageFormatter, boolean throwException) {
        this.messageFormatter = messageFormatter;
        this.definitions = new HashMap<>();
        this.functions = new FunctionRegistry(this);
        this.pragmas = new PragmaRegistry();
        this.exceptions = new ExceptionRegistry(throwException);
        this.receivers = new ReceiverRegistry();
        this.storage = new HashMap<>();
        this.validators = new HashMap<>();
    }

    public JDefinition addDefinition(JDefinition definition) {
        var previous = definitions.get(definition.getAlias());
        if(previous != null)
            throw new DuplicateDefinitionException(formatForSchema(DEFI01,
                    concat("Duplicate definition of ", quote(definition.getAlias()),
                            " is found and already defined as ", previous.getOutline()),
                    definition.getContext()));
        definitions.put(definition.getAlias(), definition.getValidator());
        return definition;
    }

    public boolean areEqual(double value1, double value2) {
        return Math.abs(value1 - value2) < pragmas.getFloatingPointTolerance();
    }

    public boolean addValidator(FutureValidator validator) {
        return validators.put(UUID.randomUUID().toString(), validator) == null;
    }

    public boolean invokeValidators() {
        var result = true;
        for(var v : validators.values()) result &= v.validate();
        return result;
    }

    public void clear() {
        exceptions.clear();
        storage.clear();
        receivers.clear();
    }
}