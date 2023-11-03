package com.relogiclabs.json.schema.tree;

import com.relogiclabs.json.schema.internal.antlr.SchemaLexer;
import com.relogiclabs.json.schema.internal.antlr.SchemaParser;
import com.relogiclabs.json.schema.internal.tree.SchemaTreeVisitor;
import com.relogiclabs.json.schema.internal.util.LexerErrorListener;
import com.relogiclabs.json.schema.internal.util.ParserErrorListener;
import com.relogiclabs.json.schema.types.JRoot;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@Getter
public final class SchemaTree {
    private final JRoot root;

    public SchemaTree(RuntimeContext context, String input) {
        var schemaLexer = new SchemaLexer(CharStreams.fromString(input));
        schemaLexer.removeErrorListeners();
        schemaLexer.addErrorListener(LexerErrorListener.SCHEMA);
        var schemaParser = new SchemaParser(new CommonTokenStream(schemaLexer));
        schemaParser.removeErrorListeners();
        schemaParser.addErrorListener(ParserErrorListener.SCHEMA);
        root = (JRoot) new SchemaTreeVisitor(context).visit(schemaParser.schema());
    }
}