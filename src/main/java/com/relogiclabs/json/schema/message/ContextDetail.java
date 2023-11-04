package com.relogiclabs.json.schema.message;

import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.Location;
import com.relogiclabs.json.schema.types.JNode;
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