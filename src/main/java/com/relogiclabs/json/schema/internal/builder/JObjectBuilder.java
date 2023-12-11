package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.collection.IndexMap;
import com.relogiclabs.json.schema.internal.collection.IndexHashMap;
import com.relogiclabs.json.schema.type.JObject;
import com.relogiclabs.json.schema.type.JProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public class JObjectBuilder extends JNodeBuilder<JObjectBuilder> {
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