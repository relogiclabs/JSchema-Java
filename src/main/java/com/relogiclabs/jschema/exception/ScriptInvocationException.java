package com.relogiclabs.jschema.exception;

import org.antlr.v4.runtime.Token;

public class ScriptInvocationException extends ScriptTemplateException {
    private final Token token;

    public ScriptInvocationException(String code, String template, Token token) {
        super(code, template);
        this.token = token;
    }

    public ScriptInvocationException(String code, String template) {
        this(code, template, null);
    }

    public Token getToken(Token defaultToken) {
        return token != null ? token : defaultToken;
    }
}