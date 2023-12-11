package com.relogiclabs.json.schema.tree;

import com.relogiclabs.json.schema.internal.antlr.JsonLexer;
import com.relogiclabs.json.schema.internal.antlr.JsonParser;
import com.relogiclabs.json.schema.internal.tree.JsonTreeVisitor;
import com.relogiclabs.json.schema.internal.util.LexerErrorListener;
import com.relogiclabs.json.schema.internal.util.ParserErrorListener;
import com.relogiclabs.json.schema.type.JRoot;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static com.relogiclabs.json.schema.tree.TreeType.JSON_TREE;

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
        result &= runtime.invokeValidators();
        return result;
    }

    @Override
    public TreeType getType() {
        return JSON_TREE;
    }
}