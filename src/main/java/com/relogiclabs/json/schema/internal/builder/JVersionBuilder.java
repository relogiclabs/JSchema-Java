package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JVersionBuilder extends JNodeBuilder<JVersionBuilder> {
    private String version;

    @Override
    public JVersion build() {
        return JVersion.from(this);
    }
}