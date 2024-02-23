package com.relogiclabs.jschema.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.Context;
import com.relogiclabs.jschema.tree.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class ContextDetail {
    private Context context;
    private String message;

    public ContextDetail(JNode node, String message) {
        this.context = node.getContext();
        this.message = message;
    }

    public Location getLocation() {
        return context.getLocation();
    }
}