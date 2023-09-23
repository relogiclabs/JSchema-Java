package com.relogiclabs.json.schema.tree;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Getter
public class Context {
    private final ParserRuleContext parser;
    private final RuntimeContext runtime;
    private final Location location;

    public Context(ParserRuleContext parser, RuntimeContext runtime) {
        this.parser = parser;
        this.runtime = runtime;
        this.location = getLocation(parser);
    }

    private Location getLocation(ParserRuleContext parser) {
        var token = parser.getStart();
        return new Location(token.getLine(), token.getCharPositionInLine());
    }
}
