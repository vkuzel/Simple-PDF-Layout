package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ChildElement;
import com.github.vkuzel.simplepdflayout.ParentElement;

import static com.github.vkuzel.simplepdflayout.util.Utils.sumHorizontalMarginBorderPadding;
import static com.github.vkuzel.simplepdflayout.util.Utils.sumVerticalMarginBorderPadding;

public final class SizeOfChildrenDimensionCalculator implements DimensionCalculator {

    private static final float MAX_VALUE = 1_000_000;
    private static final float MIN_VALUE = 0;

    private final ParentElement<?> element;
    private final Measurement measurement;

    public SizeOfChildrenDimensionCalculator(ParentElement<?> element, Measurement measurement) {
        this.element = element;
        this.measurement = measurement;
    }

    @Override
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        switch (measurement) {
            case WIDTH:
                float leftX = MAX_VALUE;
                float rightX = MIN_VALUE;
                for (ChildElement<?> child : element.getChildren()) {
                    float x = child.calculateX(calculationContext);
                    leftX = Math.min(leftX, x);
                    rightX = Math.max(rightX, x + child.calculateWidth(calculationContext));
                }
                return Math.max(rightX - leftX, 0) + sumHorizontalMarginBorderPadding(element);
            case HEIGHT:
                float topY = MAX_VALUE;
                float bottomY = MIN_VALUE;
                for (ChildElement<?> child : element.getChildren()) {
                    float y = child.calculateY(calculationContext);
                    topY = Math.min(topY, y);
                    bottomY = Math.max(bottomY, y + child.calculateHeight(calculationContext));
                }
                return Math.max(bottomY - topY, 0) + sumVerticalMarginBorderPadding(element);
            default:
                throw new IllegalStateException("Unsupported measurement " + measurement);
        }
    }

    @Override
    public String toString() {
        return "SizeOfChildrenDimensionCalculator@" + Integer.toHexString(hashCode()) + "{" +
                "element=" + element +
                ", measurement=" + measurement +
                '}';
    }
}
