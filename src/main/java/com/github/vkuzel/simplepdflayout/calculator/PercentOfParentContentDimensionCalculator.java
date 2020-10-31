package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

public final class PercentOfParentContentDimensionCalculator implements DimensionCalculator {

    private final ParentElement<?> parentElement;
    private final Measurement measurement;
    private final float dimensionPercent;

    public PercentOfParentContentDimensionCalculator(ParentElement<?> parentElement, Measurement measurement, float dimensionPercent) {
        this.parentElement = parentElement;
        this.measurement = measurement;
        this.dimensionPercent = dimensionPercent;
    }

    @Override
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        float parentContentDimension;
        switch (measurement) {
            case WIDTH:
                parentContentDimension = parentElement.calculateContentWidth(calculationContext);
                break;
            case HEIGHT:
                parentContentDimension = parentElement.calculateContentHeight(calculationContext);
                break;
            default:
                throw new IllegalStateException("Unsupported measurement " + measurement);
        }
        return dimensionPercent * parentContentDimension / 100;
    }

    @Override
    public String toString() {
        return "PercentOfParentContentDimensionCalculator@" + Integer.toHexString(hashCode()) + "{" +
                "parentElement=" + parentElement +
                ", measurement=" + measurement +
                ", dimensionPercent=" + dimensionPercent +
                '}';
    }
}
