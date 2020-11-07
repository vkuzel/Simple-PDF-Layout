package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement;

import static com.github.vkuzel.simplepdflayout.util.Utils.sumHorizontalMarginBorderPadding;
import static com.github.vkuzel.simplepdflayout.util.Utils.sumVerticalMarginBorderPadding;

public final class ContentDimensionCalculator implements Calculator {

    private final Element element;
    private final Measurement measurement;

    public ContentDimensionCalculator(Element element, Measurement measurement) {
        this.element = element;
        this.measurement = measurement;
    }

    @Override
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        float dimension;
        switch (measurement) {
            case WIDTH:
                dimension = element.calculateWidth(calculationContext);
                dimension -= sumHorizontalMarginBorderPadding(element);
                break;
            case HEIGHT:
                dimension = element.calculateHeight(calculationContext);
                dimension -= sumVerticalMarginBorderPadding(element);
                break;
            default:
                throw new IllegalStateException("Unsupported measurement " + measurement);
        }
        return dimension;
    }
}
