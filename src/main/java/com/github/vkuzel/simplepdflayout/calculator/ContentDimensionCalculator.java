package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.ElementWithBorder;
import com.github.vkuzel.simplepdflayout.ElementWithMargin;
import com.github.vkuzel.simplepdflayout.ElementWithPadding;
import com.github.vkuzel.simplepdflayout.calculator.DimensionCalculator.Measurement;
import com.github.vkuzel.simplepdflayout.property.Margin;
import com.github.vkuzel.simplepdflayout.property.Padding;

import static com.github.vkuzel.simplepdflayout.util.Utils.getValue;

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
                dimension -= getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getLeft);
                dimension -= getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getRight);
                dimension -= getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getLeft().getWidth());
                dimension -= getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getRight().getWidth());
                dimension -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getRight);
                dimension -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getRight);
                break;
            case HEIGHT:
                dimension = element.calculateHeight(calculationContext);
                dimension -= getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getTop);
                dimension -= getValue(element, ElementWithMargin.class, ElementWithMargin::getMargin, Margin::getBottom);
                dimension -= getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getTop().getWidth());
                dimension -= getValue(element, ElementWithBorder.class, ElementWithBorder::getBorder, border -> border.getBottom().getWidth());
                dimension -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getTop);
                dimension -= getValue(element, ElementWithPadding.class, ElementWithPadding::getPadding, Padding::getBottom);
                break;
            default:
                throw new IllegalStateException("Unsupported measurement " + measurement);
        }
        return dimension;
    }
}
