package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.node.JNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConstraintExtension extends AbstractExtension implements ConstraintFunctions {
    private JNode invoker;

    public <T extends JNode> T getInvoker(Class<T> type) {
        return type.cast(invoker);
    }
}