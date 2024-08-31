package com.relogiclabs.jschema.tree;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

@Getter
public final class Context {
    private final ParserRuleContext parser;
    private final RuntimeContext runtime;

    public Context(ParserRuleContext parser, RuntimeContext runtime) {
        this.parser = parser;
        this.runtime = runtime;
    }

    public Token getToken() {
        return parser.getStart();
    }
}