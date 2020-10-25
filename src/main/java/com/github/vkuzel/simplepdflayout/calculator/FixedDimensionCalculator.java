package com.github.vkuzel.simplepdflayout.calculator;

import java.util.Set;

public final class FixedDimensionCalculator implements DimensionCalculator {

    private final float dimension;

    public FixedDimensionCalculator(float dimension) {
        this.dimension = dimension;
    }

    @Override
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        return dimension;
    }
}
