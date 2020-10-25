package com.github.vkuzel.simplepdflayout.renderer;

import com.github.vkuzel.simplepdflayout.ChildElement;
import com.github.vkuzel.simplepdflayout.ParentElement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public final class ChildrenRenderer {

    private final ParentElement<?> element;

    public ChildrenRenderer(ParentElement<?> element) {
        this.element = element;
    }

    public void render(PDDocument document, PDPageContentStream contentStream) {
        for (ChildElement<?> child : element.getChildren()) {
            child.render(document, contentStream);
        }
    }
}
