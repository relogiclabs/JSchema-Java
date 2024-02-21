package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.internal.antlr.SchemaLexer;
import com.relogiclabs.jschema.internal.antlr.SchemaParser;
import com.relogiclabs.jschema.internal.engine.ScriptTreeVisitor3;
import com.relogiclabs.jschema.internal.tree.SchemaTreeVisitor;
import com.relogiclabs.jschema.internal.util.LexerErrorListener;
import com.relogiclabs.jschema.internal.util.ParserErrorListener;
import com.relogiclabs.jschema.node.JRoot;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static com.relogiclabs.jschema.tree.TreeType.SCHEMA_TREE;

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
        var schemaParseTree = schemaParser.schema();
        var scriptVisitor = new ScriptTreeVisitor3(runtime);
        var evaluator = scriptVisitor.visit(schemaParseTree);
        root = (JRoot) new SchemaTreeVisitor(scriptVisitor).visit(schemaParseTree);
        evaluator.evaluate(runtime.getScriptContext());
    }

    @Override
    public boolean match(DataTree dataTree) {
        var result = root.match(dataTree.getRoot());
        result &= runtime.invokeFutures();
        return result;
    }

    @Override
    public TreeType getType() {
        return SCHEMA_TREE;
    }
}