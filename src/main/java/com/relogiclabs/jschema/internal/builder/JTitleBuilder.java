package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JTitle;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JTitleBuilder extends JNodeBuilder<JTitleBuilder> {
    private String title;

    @Override
    public JTitle build() {
        return JTitle.from(this);
    }
}