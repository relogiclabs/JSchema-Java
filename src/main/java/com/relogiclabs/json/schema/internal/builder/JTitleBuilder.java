package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JTitle;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JTitleBuilder extends JNodeBuilder<JTitleBuilder> {
    private String title;

    @Override
    public JTitle build() {
        return JTitle.from(this);
    }
}