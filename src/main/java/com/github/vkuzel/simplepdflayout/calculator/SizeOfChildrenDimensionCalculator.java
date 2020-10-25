package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ChildElement;
import com.github.vkuzel.simplepdflayout.ParentElement;

import java.util.HashSet;
import java.util.Set;

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
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        switch (measurement) {
            case WIDTH:
                float leftX = MAX_VALUE;
                float rightX = MIN_VALUE;
                for (ChildElement<?> child : element.getChildren()) {
                    float x = child.calculateX(new HashSet<>(calculatorPath));
                    leftX = Math.min(leftX, x);
                    rightX = Math.max(rightX, x + child.calculateWidth(new HashSet<>(calculatorPath)));
                }
                return Math.max(rightX - leftX, 0);
            case HEIGHT:
                float topY = MAX_VALUE;
                float bottomY = MIN_VALUE;
                for (ChildElement<?> child : element.getChildren()) {
                    float y = child.calculateY(new HashSet<>(calculatorPath));
                    topY = Math.min(topY, y);
                    bottomY = Math.max(bottomY, y + child.calculateHeight(new HashSet<>(calculatorPath)));
                }
                return Math.max(bottomY - topY, 0);
            default:
                throw new IllegalStateException("Unsupported measurement " + measurement);
        }
    }
}
