package com.github.vkuzel.simplepdflayout.property;

import com.github.vkuzel.simplepdflayout.ChildElement;
import com.github.vkuzel.simplepdflayout.ParentElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ChildElementCollection<P extends ParentElement<P>> {

    private final P parentElement;
    private final List<ChildElement<?>> children = new ArrayList<>();

    public ChildElementCollection(P parentElement) {
        this.parentElement = parentElement;
    }

    public <C extends ChildElement<C>> P addChild(Function<P, C> childFactory, Consumer<C> childConfigurer) {
        C childElement = childFactory.apply(parentElement);
        children.add(childElement);
        childConfigurer.accept(childElement);
        return parentElement;
    }

    public List<ChildElement<?>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public ChildElement<?> getPreviousChildTo(ChildElement<?> childElement) {
        int index = children.indexOf(childElement);
        if (index < 0) {
            throw new IllegalStateException("Element " + childElement + " is not child of this element " + this);
        } else if (index > 0) {
            return children.get(index - 1);
        } else {
            return null;
        }
    }

    public ChildElement<?> getNextChildTo(ChildElement<?> childElement) {
        int index = children.indexOf(childElement);
        if (index < 0) {
            throw new IllegalStateException("Element " + childElement + " is not child of this element " + this);
        } else if (index < children.size()) {
            return children.get(index + 1);
        } else {
            return null;
        }
    }
}
