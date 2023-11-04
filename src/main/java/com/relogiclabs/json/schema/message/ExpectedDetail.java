package com.relogiclabs.json.schema.message;

import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.types.JNode;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;

public final class ExpectedDetail extends ContextDetail {
    public ExpectedDetail(Context context, String message) {
        super(context, message);
    }

    public ExpectedDetail(JNode node, String message) {
        super(node, message);
    }

    public ExpectedDetail(JNode node, Object m1, Object m2) {
        this(node, concat(m1, m2));
    }

    public ExpectedDetail(JNode node, Object m1, Object m2, Object m3) {
        this(node, concat(m1, m2, m3));
    }

    public ExpectedDetail(JNode node, Object m1, Object m2, Object m3, Object m4) {
        this(node, concat(m1, m2, m3, m4));
    }

    public ExpectedDetail(JNode node, Object m1, Object m2, Object m3, Object m4, Object m5) {
        this(node, concat(m1, m2, m3, m4, m5));
    }
}