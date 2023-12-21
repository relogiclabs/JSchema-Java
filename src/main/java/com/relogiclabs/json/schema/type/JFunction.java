package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.exception.TargetInvocationException;
import com.relogiclabs.json.schema.internal.builder.JFunctionBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.internal.tree.FunctionCache;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.InvalidNestedFunction;
import static com.relogiclabs.json.schema.internal.util.MiscellaneousHelper.nonNull;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.forEachTrue;
import static com.relogiclabs.json.schema.internal.util.StringHelper.join;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC06;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JFunction extends JBranch implements NestedMode {
    static final String NESTED_MARKER = "*";
    private final String name;
    private final boolean nested;
    private final List<JNode> arguments;
    private final FunctionCache cache;

    private JFunction(JFunctionBuilder builder) {
        super(builder);
        name = requireNonNull(builder.name());
        nested = requireNonNull(builder.nested());
        arguments = requireNonNull(builder.arguments());
        children = arguments;
        cache = new FunctionCache();
    }

    public static JFunction from(JFunctionBuilder builder) {
        return new JFunction(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        if(!nested) return invokeFunction(node);
        if(!(node instanceof JComposite composite))
            return failWith(new JsonSchemaException(
                    new ErrorDetail(FUNC06, InvalidNestedFunction),
                    ExpectedHelper.asInvalidFunction(this),
                    ActualHelper.asInvalidFunction(node)));
        return forEachTrue(composite.components().stream().map(this::invokeFunction));
    }

    private boolean invokeFunction(JNode node) {
        try {
            return getRuntime().getFunctions().invokeFunction(this, node);
        } catch(Exception ex) {
            var exception = ex instanceof TargetInvocationException ? nonNull(ex.getCause(), ex) : ex;
            if(exception instanceof RuntimeException runtimeException) throw runtimeException;
            else throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean getNested() {
        return nested;
    }

    boolean isApplicable(JNode node) {
        return !nested || node instanceof JComposite;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        if(nested) builder.append(NESTED_MARKER);
        builder.append(join(arguments, ", ", "(", ")"));
        return builder.toString();
    }
}