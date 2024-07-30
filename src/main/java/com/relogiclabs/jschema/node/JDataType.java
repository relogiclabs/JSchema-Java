package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.AliasNotFoundException;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.internal.builder.JDataTypeBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.internal.util.MatchResult;
import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.message.MessageHelper.DataTypeArgumentFailed;
import static com.relogiclabs.jschema.internal.message.MessageHelper.DataTypeMismatched;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.jschema.message.ErrorCode.ALSDEF02;
import static com.relogiclabs.jschema.message.ErrorCode.ALSDEF03;
import static com.relogiclabs.jschema.message.ErrorCode.DTYARG01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYARG02;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS02;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

@Getter
@EqualsAndHashCode
public final class JDataType extends JBranch implements NestedMode {
    static final String NESTED_MARKER = "*";
    private final JsonType jsonType;
    private final JAlias alias;
    private final boolean nested;

    private JDataType(JDataTypeBuilder builder) {
        super(builder);
        jsonType = requireNonNull(builder.jsonType());
        nested = requireNonNull(builder.nested());
        alias = builder.alias();
        children = asList(alias);
    }

    public static JDataType from(JDataTypeBuilder builder) {
        return new JDataType(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        var result = jsonType.match(node);
        if(!result.isSuccess()) return failOnType(new DataTypeValidationException(
            new ErrorDetail(nested ? DTYPMS02 : DTYPMS01, formatMessage(result)),
            ExpectedHelper.asDataTypeMismatched(this, result),
            ActualHelper.asDataTypeMismatched(node)));
        if(alias == null) return true;
        var validator = getRuntime().getDefinitions().get(alias);
        if(validator == null) return fail(new AliasNotFoundException(formatForSchema(
            nested ? ALSDEF03 : ALSDEF02, "No definition found for '" + alias + "'", this)));
        if(!validator.match(node)) return fail(new DataTypeValidationException(
            new ErrorDetail(nested ? DTYARG02 : DTYARG01, DataTypeArgumentFailed),
            ExpectedHelper.asDataTypeArgumentFailed(this),
            ActualHelper.asDataTypeArgumentFailed(node)));
        return true;
    }

    private boolean failOnType(DataTypeValidationException exception) {
        exception.setTypeBaseName(toString(true));
        return fail(exception);
    }

    private static String formatMessage(MatchResult result) {
        if(result.getNote() == null) return DataTypeMismatched;
        return DataTypeMismatched + " (" + uncapitalize(result.getNote()) + ")";
    }

    boolean isApplicable(JNode node) {
        return !nested || node instanceof JComposite;
    }

    boolean isMatchNull() {
        return !nested && jsonType.isNullType();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean baseForm) {
        var builder = new StringBuilder(jsonType.toString());
        if(nested && !baseForm) builder.append(NESTED_MARKER);
        if(alias != null && !baseForm) builder.append("(").append(alias).append(")");
        return builder.toString();
    }
}