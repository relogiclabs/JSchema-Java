package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.tree.RuntimeContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public interface ScriptTreeVisitor {
    RuntimeContext getRuntime();
    ParseTreeProperty<Evaluator> getScripts();
}