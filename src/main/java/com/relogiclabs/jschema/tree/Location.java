package com.relogiclabs.jschema.tree;

import org.antlr.v4.runtime.Token;

public record Location(int line, int column) {
    public static Location from(Token token) {
        return new Location(token.getLine(), token.getCharPositionInLine());
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}