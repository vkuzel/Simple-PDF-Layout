package com.github.vkuzel.simplepdflayout.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CalculationContext {

    private final Map<Calculator, Float> calculationMap = new HashMap<>();

    // TODO Add protection for element graph cycles... "calculators in calculation"
    public float compute(Calculator calculator, Function<CalculationContext, Float> valueFunction) {
        // This methods cannot use map's `compute*` methods since Java 9,
        // because "The mapping function should not modify this map during
        // computation."
        Float value = calculationMap.get(calculator);
        if (value == null) {
            value = valueFunction.apply(this);
            calculationMap.put(calculator, value);
        }
        return value;
    }
}
