package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.exception.TargetInvocationException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.InvalidNestedFunction;
import static com.relogiclabs.json.schema.internal.util.MiscellaneousHelper.nonNull;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.allTrue;
import static com.relogiclabs.json.schema.internal.util.StringHelper.toUnitedString;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC06;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JFunction extends JBranch implements NestedMode {
    public final String name;
    public final List<JNode> arguments;
    public final boolean nested;

    public JFunction(Builder builder) {
        super(builder.relations, builder.context);
        this.name = requireNonNull(builder.name);
        this.arguments = requireNonNull(builder.arguments);
        this.nested = builder.nested;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Collection<? extends JNode> getChildren() {
        return arguments;
    }

    @Override
    public boolean match(JNode node) {
        if(!nested) return invokeFunction(node);
        if(!(node instanceof JComposite composite))
            return failWith(new JsonSchemaException(
                    new ErrorDetail(FUNC06, InvalidNestedFunction),
                    ExpectedHelper.asInvalidFunction(this),
                    ActualHelper.asInvalidFunction(node)));
        return allTrue(composite.components().stream().map(this::invokeFunction));
    }

    private boolean invokeFunction(JNode node) {
        try {
            return getRuntime().invokeFunction(this, node);
        } catch(Exception ex) {
            var cause = ex instanceof TargetInvocationException ? nonNull(ex.getCause(), ex) : ex;
            if(cause instanceof JsonSchemaException jsonSchema) throw jsonSchema;
            throw ex;
        }
    }

    @Override
    public boolean getNested() {
        return nested;
    }

    public boolean isApplicable(JNode node) {
        return !nested || node instanceof JComposite;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        if(nested) builder.append(NestedMode.NESTED_MARKER);
        builder.append(toUnitedString(arguments, ", ", "(", ")"));
        return builder.toString();
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        public String name;
        public List<JNode> arguments;
        public boolean nested;

        @Override
        public JFunction build() {
            return new JFunction(this).initialize();
        }
    }
}