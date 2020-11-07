package com.github.vkuzel.simplepdflayout.calculator;

public final class FixedDimensionCalculator implements DimensionCalculator {

    private final float dimension;

    public FixedDimensionCalculator(float dimension) {
        this.dimension = dimension;
    }

    @Override
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, cc -> dimension);
    }
}
