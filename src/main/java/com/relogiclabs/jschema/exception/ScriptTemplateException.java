package com.relogiclabs.jschema.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptTemplateException extends ScriptCommonException {
    private static final Pattern TEMPLATE_PATTERN = Pattern.compile(" '?%s'?");
    private final String template;

    public ScriptTemplateException(String code, String template) {
        super(code, toMessage(template));
        this.template = template;
    }

    public String getMessage(Object... args) {
        return template.formatted(args);
    }

    private static String toMessage(String template) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(template);
        if(matcher.find()) return matcher.replaceAll("");
        return template;
    }
}