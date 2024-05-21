package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.builder.JValidatorBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.relogiclabs.jschema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.jschema.internal.message.MessageHelper.InvalidNonCompositeType;
import static com.relogiclabs.jschema.internal.message.MessageHelper.ValidationFailed;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.addToList;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.tryGetLast;
import static com.relogiclabs.jschema.internal.util.StreamHelper.forEachTrue;
import static com.relogiclabs.jschema.internal.util.StringHelper.join;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP03;
import static com.relogiclabs.jschema.message.ErrorCode.VALD01;
import static com.relogiclabs.jschema.node.JDataType.DATA_TYPE_NAME;
import static java.util.Collections.unmodifiableCollection;
import static lombok.AccessLevel.NONE;

@Getter
@EqualsAndHashCode
public final class JValidator extends JBranch {
    static final String OPTIONAL_MARKER = "?";

    private final JNode value;
    private final List<JFunction> functions;
    private final List<JDataType> dataTypes;
    private final List<JReceiver> receivers;
    private final boolean optional;

    @Getter(NONE)
    @EqualsAndHashCode.Exclude
    private final List<Exception> exceptions;

    private JValidator(JValidatorBuilder builder) {
        super(builder);
        value = builder.value();
        functions = builder.functions();
        dataTypes = builder.dataTypes();
        receivers = builder.receivers();
        optional = builder.optional();
        exceptions = new LinkedList<>();
        getRuntime().getReceivers().register(receivers);
        var nodes = new ArrayList<JNode>();
        addToList(nodes, value);
        addToList(nodes, functions, dataTypes, receivers);
        children = unmodifiableCollection(nodes);
    }

    public static JValidator from(JValidatorBuilder builder) {
        return new JValidator(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        boolean rValue = true;
        var other = castType(node, JsonTypable.class);
        if(other == null) return false;
        getRuntime().getReceivers().receive(receivers, node);
        if(node instanceof JNull && dataTypes.stream()
                .anyMatch(JDataType::isMatchNull)) return true;
        if(value != null) rValue &= value.match(other.getNode());
        if(!rValue) return fail(new JsonSchemaException(
                new ErrorDetail(VALD01, ValidationFailed),
                ExpectedHelper.asGeneralValueMismatch(value),
                ActualHelper.asGeneralValueMismatch(node)));
        var rDataType = matchDataType(node);
        var fDataType = rDataType && !dataTypes.isEmpty();
        boolean rFunction = forEachTrue(functions.stream()
                .filter(f -> f.isApplicable(node) || !fDataType)
                .map(f -> f.match(node)));
        return rValue & rDataType & rFunction;
    }

    private boolean matchDataType(JNode node) {
        if(getRuntime().getExceptions().tryExecute(() -> checkDataType(node))) return true;
        saveTryBuffer();
        for(var e : exceptions) fail((RuntimeException) e);
        return false;
    }

    private static List<Exception> processTryBuffer(List<Exception> buffer) {
        var list = new ArrayList<Exception>(buffer.size());
        for(var e : buffer) {
            var result = mergeException(tryGetLast(list), e);
            if(result != null) list.set(list.size() - 1, result);
            else list.add(e);
        }
        return list;
    }

    private static JsonSchemaException mergeException(Exception ex1, Exception ex2) {
        if(!(ex1 instanceof JsonSchemaException e1)) return null;
        if(!(ex2 instanceof JsonSchemaException e2)) return null;
        if(!e1.getCode().equals(e2.getCode())) return null;
        var a1 = e1.getAttribute(DATA_TYPE_NAME);
        var a2 = e2.getAttribute(DATA_TYPE_NAME);
        if(a1 == null || a2 == null) return null;
        var cause = ex1.getCause() != null ? ex1.getCause() : ex1;
        cause.addSuppressed(ex2);
        var result = new JsonSchemaException(new ErrorDetail(e1.getCode(), DataTypeMismatch),
            mergeExpected(e1, e2), e2.getActual(), cause);
        result.setAttribute(DATA_TYPE_NAME, a1 + a2);
        return result;
    }

    private static ExpectedDetail mergeExpected(JsonSchemaException ex1,
                                                JsonSchemaException ex2) {
        var typeName2 = ex2.getAttribute(DATA_TYPE_NAME);
        var expected1 = ex1.getExpected();
        return new ExpectedDetail(expected1.getContext(),
            expected1.getMessage() + " or " + typeName2);
    }

    private boolean checkDataType(JNode node) {
        var list1 = dataTypes.stream().filter(d -> !d.isNested()).toList();
        var result1 = anyMatch(list1, node);
        if(result1) getTryBuffer().clear();
        var list2 = dataTypes.stream().filter(d -> d.isNested()
                && (d.isApplicable(node) || !result1)).toList();
        if(list2.isEmpty()) return result1 || list1.isEmpty();
        if(!(node instanceof JComposite composite))
            return fail(new JsonSchemaException(
                    new ErrorDetail(DTYP03, InvalidNonCompositeType),
                    ExpectedHelper.asInvalidNonCompositeType(list2.get(0)),
                    ActualHelper.asInvalidNonCompositeType(node)));
        saveTryBuffer();
        var result2 = forEachTrue(composite.components().stream()
                .map(n -> anyMatch(list2, n)));
        return (result1 || list1.isEmpty()) && result2;
    }

    private boolean anyMatch(List<JDataType> list, JNode node) {
        getTryBuffer().clear();
        for(var d : list) if(d.match(node)) return true;
        saveTryBuffer();
        return false;
    }

    private List<Exception> getTryBuffer() {
        return getRuntime().getExceptions().getTryBuffer();
    }

    private void saveTryBuffer() {
        List<Exception> tryBuffer = getTryBuffer();
        if(tryBuffer.isEmpty()) return;
        exceptions.addAll(processTryBuffer(tryBuffer));
        tryBuffer.clear();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(value != null) builder.append(value);
        builder.append(join(functions, " ", " "));
        builder.append(join(dataTypes, " ", " "));
        if(optional) builder.append(" " + OPTIONAL_MARKER);
        return builder.toString().trim();
    }
}