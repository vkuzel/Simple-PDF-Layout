package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.calculator.PositionCalculator.Axis;

import static com.github.vkuzel.simplepdflayout.util.Utils.sumLeftMarginBorderPadding;
import static com.github.vkuzel.simplepdflayout.util.Utils.sumTopMarginBorderPadding;

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
                position += sumLeftMarginBorderPadding(element);
                break;
            case Y:
                position = element.calculateY(calculationContext);
                position += sumTopMarginBorderPadding(element);
                break;
            default:
                throw new IllegalStateException("Unsupported axis " + axis);
        }
        return position;
    }
}
