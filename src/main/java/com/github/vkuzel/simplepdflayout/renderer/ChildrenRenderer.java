package com.github.vkuzel.simplepdflayout.renderer;

import com.github.vkuzel.simplepdflayout.ChildElement;
import com.github.vkuzel.simplepdflayout.ParentElement;

public final class ChildrenRenderer {

    private final ParentElement<?> element;

    public ChildrenRenderer(ParentElement<?> element) {
        this.element = element;
    }

    public void render(RenderingContext renderingContext) {
        for (ChildElement<?> child : element.getChildren()) {
            child.render(renderingContext);
        }
    }
}
