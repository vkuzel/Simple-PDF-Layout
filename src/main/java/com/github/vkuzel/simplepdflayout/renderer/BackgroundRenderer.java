package com.github.vkuzel.simplepdflayout.renderer;

import com.github.vkuzel.simplepdflayout.ElementWithBackground;
import com.github.vkuzel.simplepdflayout.ElementWithPadding;
import com.github.vkuzel.simplepdflayout.property.Padding;
import com.github.vkuzel.simplepdflayout.property.Point;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;

import static com.github.vkuzel.simplepdflayout.util.Utils.getValue;

public final class BackgroundRenderer {

    private final ElementWithBackground element;

    public BackgroundRenderer(ElementWithBackground element) {
        this.element = element;
    }

    public void render(PDPageContentStream contentStream) {
        Color backgroundColor = element.getBackgroundColor();
        if (backgroundColor == null) {
            return;
        }

        float x = calculateX();
        float y = calculateY();
        float width = calculateWidth();
        float height = calculateHeight();

        Point bottomLeft = Point.of(x, y + height);
        Point pdfBottomLeft = element.convertPointToPdfCoordinates(bottomLeft);

        try {
            contentStream.addRect(pdfBottomLeft.getX(), pdfBottomLeft.getY(), width, height);
            contentStream.setNonStrokingColor(backgroundColor);
            contentStream.fill();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private float calculateX() {
        float x = element.calculateContentX(new HashSet<>());
        x -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getLeft);
        return x;
    }

    private float calculateY() {
        float y = element.calculateContentY(new HashSet<>());
        y -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
        return y;
    }

    private float calculateWidth() {
        float width = element.calculateContentWidth(new HashSet<>());
        width += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getLeft);
        width += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getRight);
        return width;
    }

    private float calculateHeight() {
        float height = element.calculateContentHeight(new HashSet<>());
        height += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
        height += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getBottom);
        return height;
    }
}
