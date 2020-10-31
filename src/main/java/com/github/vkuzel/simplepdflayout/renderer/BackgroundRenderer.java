package com.github.vkuzel.simplepdflayout.renderer;

import com.github.vkuzel.simplepdflayout.ElementWithBackground;
import com.github.vkuzel.simplepdflayout.ElementWithPadding;
import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import com.github.vkuzel.simplepdflayout.property.Padding;
import com.github.vkuzel.simplepdflayout.property.Point;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

import static com.github.vkuzel.simplepdflayout.util.Utils.getValue;

public final class BackgroundRenderer {

    private final ElementWithBackground element;

    public BackgroundRenderer(ElementWithBackground element) {
        this.element = element;
    }

    public void render(RenderingContext renderingContext) {
        Color backgroundColor = element.getBackgroundColor();
        if (backgroundColor == null) {
            return;
        }

        CalculationContext calculationContext = renderingContext.getCalculationContext();
        float x = calculateX(calculationContext);
        float y = calculateY(calculationContext);
        float width = calculateWidth(calculationContext);
        float height = calculateHeight(calculationContext);

        Point bottomLeft = Point.of(x, y + height);
        Point pdfBottomLeft = element.convertPointToPdfCoordinates(bottomLeft);

        try {
            PDPageContentStream contentStream = renderingContext.getContentStream();
            contentStream.addRect(pdfBottomLeft.getX(), pdfBottomLeft.getY(), width, height);
            contentStream.setNonStrokingColor(backgroundColor);
            contentStream.fill();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private float calculateX(CalculationContext calculationContext) {
        float x = element.calculateContentX(calculationContext);
        x -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getLeft);
        return x;
    }

    private float calculateY(CalculationContext calculationContext) {
        float y = element.calculateContentY(calculationContext);
        y -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
        return y;
    }

    private float calculateWidth(CalculationContext calculationContext) {
        float width = element.calculateContentWidth(calculationContext);
        width += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getLeft);
        width += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getRight);
        return width;
    }

    private float calculateHeight(CalculationContext calculationContext) {
        float height = element.calculateContentHeight(calculationContext);
        height += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
        height += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getBottom);
        return height;
    }
}
