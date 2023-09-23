package com.relogiclabs.json.schema.function;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JFunction;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class FunctionBase {
    protected final RuntimeContext runtime;

    @Setter
    protected JFunction function;

    public FunctionBase(RuntimeContext runtime) {
        this.runtime = runtime;
    }

    protected boolean failWith(JsonSchemaException exception) {
        return runtime.failWith(exception);
    }
}