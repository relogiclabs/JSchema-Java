package com.relogiclabs.jschema.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.Context;

public final class ActualDetail extends ContextDetail {
    public ActualDetail(Context context, String message) {
        super(context, message);
    }

    public ActualDetail(JNode node, String message) {
        super(node, message);
    }
}