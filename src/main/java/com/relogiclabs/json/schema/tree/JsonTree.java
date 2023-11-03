package com.relogiclabs.json.schema.tree;

import com.relogiclabs.json.schema.internal.antlr.JsonLexer;
import com.relogiclabs.json.schema.internal.antlr.JsonParser;
import com.relogiclabs.json.schema.internal.tree.JsonTreeVisitor;
import com.relogiclabs.json.schema.internal.util.LexerErrorListener;
import com.relogiclabs.json.schema.internal.util.ParserErrorListener;
import com.relogiclabs.json.schema.types.JRoot;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@Getter
public final class JsonTree {
    private final JRoot root;

    public JsonTree(RuntimeContext context, String input) {
        var jsonLexer = new JsonLexer(CharStreams.fromString(input));
        jsonLexer.removeErrorListeners();
        jsonLexer.addErrorListener(LexerErrorListener.JSON);
        var jsonParser = new JsonParser(new CommonTokenStream(jsonLexer));
        jsonParser.removeErrorListeners();
        jsonParser.addErrorListener(ParserErrorListener.JSON);
        root = (JRoot) new JsonTreeVisitor(context).visit(jsonParser.json());
    }
}