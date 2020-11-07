package com.github.vkuzel.simplepdflayout;

import com.github.vkuzel.simplepdflayout.calculator.CalculationContext;
import com.github.vkuzel.simplepdflayout.property.Dimension;
import com.github.vkuzel.simplepdflayout.property.Point;
import com.github.vkuzel.simplepdflayout.renderer.RenderingContext;

public interface Element {

    float calculateX(CalculationContext calculationContext);

    float calculateY(CalculationContext calculationContext);

    default Point calculateTopLeft() {
        CalculationContext calculationContext = new CalculationContext();
        float x = calculateX(calculationContext);
        float y = calculateY(calculationContext);
        return Point.of(x, y);
    }

    float calculateWidth(CalculationContext calculationContext);

    float calculateHeight(CalculationContext calculationContext);

    default Dimension calculateDimension() {
        CalculationContext calculationContext = new CalculationContext();
        float width = calculateWidth(calculationContext);
        float height = calculateHeight(calculationContext);
        return Dimension.of(width, height);
    }

    float calculateContentX(CalculationContext calculationContext);

    float calculateContentY(CalculationContext calculationContext);

    default Point calculateContentTopLeft() {
        CalculationContext calculationContext = new CalculationContext();
        float x = calculateContentX(calculationContext);
        float y = calculateContentY(calculationContext);
        return Point.of(x, y);
    }

    float calculateContentWidth(CalculationContext calculationContext);

    float calculateContentHeight(CalculationContext calculationContext);

    default Dimension calculateContentDimension() {
        CalculationContext calculationContext = new CalculationContext();
        float width = calculateContentWidth(calculationContext);
        float height = calculateContentHeight(calculationContext);
        return Dimension.of(width, height);
    }

    void render(RenderingContext renderingContext);

    default Point convertPointToPdfCoordinates(Point point) {
        if (this instanceof ChildElement) {
            return ((ChildElement<?>) this).getParent().convertPointToPdfCoordinates(point);
        } else {
            throw new UnsupportedOperationException("Not implemented!");
        }
    }
}
