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

import static com.relogiclabs.json.schema.tree.TreeType.SCHEMA_TREE;

@Getter
public final class SchemaTree implements DataTree {
    private final RuntimeContext runtime;
    private final JRoot root;

    public SchemaTree(RuntimeContext runtime, String input) {
        this.runtime = runtime;
        var schemaLexer = new SchemaLexer(CharStreams.fromString(input));
        schemaLexer.removeErrorListeners();
        schemaLexer.addErrorListener(LexerErrorListener.SCHEMA);
        var schemaParser = new SchemaParser(new CommonTokenStream(schemaLexer));
        schemaParser.removeErrorListeners();
        schemaParser.addErrorListener(ParserErrorListener.SCHEMA);
        root = (JRoot) new SchemaTreeVisitor(runtime).visit(schemaParser.schema());
    }

    @Override
    public boolean match(DataTree dataTree) {
        var result = root.match(dataTree.getRoot());
        result &= runtime.invokeValidators();
        return result;
    }

    @Override
    public TreeType getType() {
        return SCHEMA_TREE;
    }
}