package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

import java.util.Set;

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
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        float parentContentDimension;
        switch (measurement) {
            case WIDTH:
                parentContentDimension = parentElement.calculateContentWidth(calculatorPath);
                break;
            case HEIGHT:
                parentContentDimension = parentElement.calculateContentHeight(calculatorPath);
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
