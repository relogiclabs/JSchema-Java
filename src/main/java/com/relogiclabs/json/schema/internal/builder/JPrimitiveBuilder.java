package com.relogiclabs.json.schema.internal.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public abstract class JPrimitiveBuilder<T> extends JNodeBuilder<JPrimitiveBuilder<T>> {
    private T value;
}