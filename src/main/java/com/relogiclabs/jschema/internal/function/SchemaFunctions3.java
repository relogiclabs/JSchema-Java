package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JArray;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import java.net.URI;
import java.util.regex.Pattern;

import static com.relogiclabs.jschema.internal.util.CollectionHelper.containsKeys;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.containsValues;
import static com.relogiclabs.jschema.internal.util.StreamHelper.count;
import static com.relogiclabs.jschema.internal.util.StringHelper.join;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;
import static com.relogiclabs.jschema.message.ErrorCode.ELEMCF01;
import static com.relogiclabs.jschema.message.ErrorCode.EMALCF01;
import static com.relogiclabs.jschema.message.ErrorCode.KEYFND01;
import static com.relogiclabs.jschema.message.ErrorCode.PHONCF01;
import static com.relogiclabs.jschema.message.ErrorCode.REGXCF01;
import static com.relogiclabs.jschema.message.ErrorCode.URLADR01;
import static com.relogiclabs.jschema.message.ErrorCode.URLADR02;
import static com.relogiclabs.jschema.message.ErrorCode.URLSCM01;
import static com.relogiclabs.jschema.message.ErrorCode.URLSCM02;
import static com.relogiclabs.jschema.message.ErrorCode.VALFND01;
import static java.util.Arrays.stream;

public abstract class SchemaFunctions3 extends SchemaFunctions2 {
    private static final String URI_SCHEME_HTTPS = "https";
    private static final String URI_SCHEME_HTTP = "http";

    private static final Pattern EMAIL_REGEX
            = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final Pattern PHONE_REGEX
            = Pattern.compile("\\+?[0-9 ()-]+");

    @ConstraintFunction
    public boolean elements(JArray target, EValue... items) {
        return count(stream(items)
            .filter(n -> !target.elements().contains(n))
            .map(n -> failOnElement(target, n))) == 0;
    }

    private boolean failOnElement(JArray target, EValue item) {
        return fail(new FunctionValidationException(
            new ErrorDetail(ELEMCF01, "Element not found in target array"),
            new ExpectedDetail(invoker, "an array with element " + item),
            new ActualDetail(target, "not found in target " + getOutline(target))));
    }

    @ConstraintFunction
    public boolean keys(JObject target, EString... items) {
        return count(containsKeys(target.getProperties(), items)
            .stream().map(s -> failOnKey(target, s))) == 0;
    }

    private boolean failOnKey(JObject target, String key) {
        return fail(new FunctionValidationException(
            new ErrorDetail(KEYFND01, "Key not found in target object"),
            new ExpectedDetail(invoker, "an object with key " + quote(key)),
            new ActualDetail(target, "not found in target " + getOutline(target))));
    }

    @ConstraintFunction
    public boolean values(JObject target, EValue... items) {
        return count(containsValues(target.getProperties(), items)
            .stream().map(n -> failOnValue(target, n))) == 0;
    }

    private boolean failOnValue(JObject target, EValue item) {
        return fail(new FunctionValidationException(
            new ErrorDetail(VALFND01, "Value not found in target object"),
            new ExpectedDetail(invoker, "an object with value " + item),
            new ActualDetail(target, "not found in target " + getOutline(target))));
    }

    @ConstraintFunction
    public boolean regex(JString target, EString pattern) {
        if(!Pattern.matches(pattern.getValue(), target.getValue()))
            return fail(new FunctionValidationException(
                new ErrorDetail(REGXCF01, "Target mismatched with regex pattern"),
                new ExpectedDetail(invoker, "a string following pattern " + pattern),
                new ActualDetail(target, "mismatched for target " + getOutline(target))));
        return true;
    }

    @ConstraintFunction
    public boolean email(JString target) {
        // Based on SMTP protocol RFC 5322
        if(!EMAIL_REGEX.matcher(target.getValue()).matches())
            return fail(new FunctionValidationException(
                new ErrorDetail(EMALCF01, "Invalid target email address"),
                new ExpectedDetail(invoker, "a valid email address"),
                new ActualDetail(target, "found malformed target " + target)));
        return true;
    }

    @ConstraintFunction
    public boolean url(JString target) {
        URI uri;
        try {
            uri = new URI(target.getValue());
        } catch (Exception e) {
            return fail(new FunctionValidationException(
                new ErrorDetail(URLADR01, "Invalid target URL address"),
                new ExpectedDetail(invoker, "a valid URL address"),
                new ActualDetail(target, "found malformed target " + target)));
        }
        var result = switch(uri.getScheme()) {
            case URI_SCHEME_HTTP, URI_SCHEME_HTTPS -> true;
            default -> false;
        };
        if(!result) return fail(new FunctionValidationException(
            new ErrorDetail(URLADR02, "Mismatched target URL address scheme"),
            new ExpectedDetail(invoker, "A URL with HTTP or HTTPS scheme"),
            new ActualDetail(target, "found scheme " + quote(uri.getScheme())
                + " for target " + target)));
        return true;
    }

    @ConstraintFunction
    public boolean url(JString target, EString... schemes) {
        URI uri;
        try {
            uri = new URI(target.getValue());
        } catch (Exception e) {
            return fail(new FunctionValidationException(
                new ErrorDetail(URLSCM01, "Invalid target URL address"),
                new ExpectedDetail(invoker, "a valid URL address"),
                new ActualDetail(target, "found malformed target " + target)));
        }
        if(stream(schemes).map(EString::getValue)
            .anyMatch(s -> s.equals(uri.getScheme()))) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(URLSCM02, "Mismatched target URL address scheme"),
            new ExpectedDetail(invoker, "A URL with scheme " + join(stream(schemes), " or ")),
            new ActualDetail(target, "found scheme " + quote(uri.getScheme())
                + " for target " + target)));
    }

    @ConstraintFunction
    public boolean phone(JString target) {
        // Based on ITU-T E.163 and E.164 (including widely used)
        if(!PHONE_REGEX.matcher(target.getValue()).matches())
            return fail(new FunctionValidationException(
                new ErrorDetail(PHONCF01, "Invalid target phone number format"),
                new ExpectedDetail(invoker, "a valid phone number"),
                new ActualDetail(target, "found malformed target " + target)));
        return true;
    }
}