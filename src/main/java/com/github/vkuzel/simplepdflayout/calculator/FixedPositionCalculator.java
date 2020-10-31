package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

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
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        switch (axis) {
            case X:
                return parentElement.calculateContentX(calculationContext) + position;
            case Y:
                return parentElement.calculateContentY(calculationContext) + position;
            default:
                throw new IllegalStateException("Unsupported axis " + axis);
        }
    }

    @Override
    public String toString() {
        return "FixedPositionCalculator@" + Integer.toHexString(hashCode()) + "{" +
                "parentElement=" + parentElement +
                ", axis=" + axis +
                ", position=" + position +
                '}';
    }
}
