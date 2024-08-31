package com.relogiclabs.jschema.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.Context;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antlr.v4.runtime.Token;

@Getter
@AllArgsConstructor
public abstract class ContextDetail {
    private Context context;
    private String message;

    public ContextDetail(JNode node, String message) {
        this.context = node.getContext();
        this.message = message;
    }

    public String getLocation() {
        return getLocation(context.getToken());
    }

    public static String getLocation(Token token) {
        return token.getLine() + ":" + token.getCharPositionInLine();
    }
}