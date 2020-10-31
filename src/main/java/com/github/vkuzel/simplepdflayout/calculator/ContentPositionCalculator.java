package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.ElementWithBorder;
import com.github.vkuzel.simplepdflayout.ElementWithMargin;
import com.github.vkuzel.simplepdflayout.ElementWithPadding;
import com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis;
import com.github.vkuzel.simplepdflayout.property.Margin;
import com.github.vkuzel.simplepdflayout.property.Padding;

import static com.github.vkuzel.simplepdflayout.util.Utils.getValue;

public final class ContentPositionCalculator implements Calculator {

    private final Element element;
    private final Axis axis;

    public ContentPositionCalculator(Element element, Axis axis) {
        this.element = element;
        this.axis = axis;
    }

    @Override
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        float position;
        switch (axis) {
            case X:
                position = element.calculateX(calculationContext);
                position += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getLeft);
                position += getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getLeft().getWidth());
                position += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getLeft);
                break;
            case Y:
                position = element.calculateY(calculationContext);
                position += getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getTop);
                position += getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getTop().getWidth());
                position += getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
                break;
            default:
                throw new IllegalStateException("Unsupported axis " + axis);
        }
        return position;
    }
}
