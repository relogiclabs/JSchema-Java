package com.relogiclabs.jschema.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.Context;

public final class ExpectedDetail extends ContextDetail {
    public ExpectedDetail(Context context, String message) {
        super(context, message);
    }

    public ExpectedDetail(JNode node, String message) {
        super(node, message);
    }
}