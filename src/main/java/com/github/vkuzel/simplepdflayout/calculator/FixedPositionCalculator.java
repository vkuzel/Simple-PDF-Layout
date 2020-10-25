package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

import java.util.Set;

public final class FixedPositionCalculator implements PositionCalculator {

    private final ParentElement<?> parentElement;
    private final Axis axis;
    private final float position;

    public FixedPositionCalculator(ParentElement<?> parentElement, Axis axis, float position) {
        this.parentElement = parentElement;
        this.axis = axis;
        this.position = position;
    }

    @Override
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        switch (axis) {
            case X:
                return parentElement.calculateContentX(calculatorPath) + position;
            case Y:
                return parentElement.calculateContentY(calculatorPath) + position;
            default:
                throw new IllegalStateException("Unsupported axis " + axis);
        }
    }
}
