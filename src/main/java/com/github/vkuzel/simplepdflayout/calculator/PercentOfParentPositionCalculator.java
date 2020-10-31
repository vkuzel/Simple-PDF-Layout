package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

public final class PercentOfParentPositionCalculator implements PositionCalculator {

    private final ParentElement<?> parentElement;
    private final Axis axis;
    private final float positionPercent;

    public PercentOfParentPositionCalculator(ParentElement<?> parentElement, Axis axis, float positionPercent) {
        this.parentElement = parentElement;
        this.axis = axis;
        this.positionPercent = positionPercent;
    }

    @Override
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        float parentContentPosition;
        float parentContentDimension;
        switch (axis) {
            case X:
                parentContentPosition = parentElement.calculateContentX(calculationContext);
                parentContentDimension = parentElement.calculateContentWidth(calculationContext);
                break;
            case Y:
                parentContentPosition = parentElement.calculateContentY(calculationContext);
                parentContentDimension = parentElement.calculateContentHeight(calculationContext);
                break;
            default:
                throw new IllegalStateException("Unsupported axis " + axis);
        }
        return parentContentPosition + positionPercent * parentContentDimension / 100;
    }

    @Override
    public String toString() {
        return "PercentOfParentPositionCalculator@" + Integer.toHexString(hashCode()) + "{" +
                "parentElement=" + parentElement +
                ", axis=" + axis +
                ", positionPercent=" + positionPercent +
                '}';
    }
}
