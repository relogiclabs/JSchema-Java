package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.internal.antlr.JsonLexer;
import com.relogiclabs.jschema.internal.antlr.JsonParser;
import com.relogiclabs.jschema.internal.tree.JsonTreeVisitor;
import com.relogiclabs.jschema.internal.util.LexerErrorListener;
import com.relogiclabs.jschema.internal.util.ParserErrorListener;
import com.relogiclabs.jschema.node.JRoot;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static com.relogiclabs.jschema.tree.TreeType.JSON_TREE;

@Getter
public final class JsonTree implements DataTree {
    private final RuntimeContext runtime;
    private final JRoot root;

    public JsonTree(RuntimeContext runtime, String input) {
        this.runtime = runtime;
        var jsonLexer = new JsonLexer(CharStreams.fromString(input));
        jsonLexer.removeErrorListeners();
        jsonLexer.addErrorListener(LexerErrorListener.JSON);
        var jsonParser = new JsonParser(new CommonTokenStream(jsonLexer));
        jsonParser.removeErrorListeners();
        jsonParser.addErrorListener(ParserErrorListener.JSON);
        root = (JRoot) new JsonTreeVisitor(runtime).visit(jsonParser.json());
    }

    @Override
    public boolean match(DataTree dataTree) {
        var result = root.match(dataTree.getRoot());
        result &= runtime.invokeFutures();
        return result;
    }

    @Override
    public TreeType getType() {
        return JSON_TREE;
    }
}