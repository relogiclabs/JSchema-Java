package com.relogiclabs.json.schema.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antlr.v4.runtime.Token;

@Getter
@AllArgsConstructor
public class Location {
    private int line;
    private int column;

    public static Location from(Token token) {
        return new Location(token.getLine(), token.getCharPositionInLine());
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
