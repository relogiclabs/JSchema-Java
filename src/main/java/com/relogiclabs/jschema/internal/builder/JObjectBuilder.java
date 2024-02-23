package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.collection.IndexMap;
import com.relogiclabs.jschema.internal.collection.IndexHashMap;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public final class JObjectBuilder extends JNodeBuilder<JObjectBuilder> {
    private IndexMap<String, JProperty> properties;

    public JObjectBuilder properties(List<JProperty> properties) {
        this.properties = new IndexHashMap<>(properties).asUnmodifiable();
        return this;
    }

    @Override
    public JObject build() {
        return JObject.from(this);
    }
}