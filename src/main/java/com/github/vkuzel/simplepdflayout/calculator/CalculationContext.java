package com.github.vkuzel.simplepdflayout.calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CalculationContext {

    private static final String CYCLE_DEPENDENCIES_MSG = "Circular element dependencies!\n" +
            "Calculator: %s\n" +
            "All calculators: %s";

    private final Set<Calculator> calculatingCalculators = new HashSet<>();
    private final Map<Calculator, Float> calculationMap = new HashMap<>();

    public float compute(Calculator calculator, Function<CalculationContext, Float> valueFunction) {
        // This methods cannot use map's `compute*` methods since Java 9,
        // because "The mapping function should not modify this map during
        // computation."
        Float value = calculationMap.get(calculator);
        if (value == null) {
            if (!calculatingCalculators.add(calculator)) {
                throwCircularDependenciesException(calculator);
            }
            value = valueFunction.apply(this);
            calculationMap.put(calculator, value);
            calculatingCalculators.remove(calculator);
        }
        return value;
    }

    private void throwCircularDependenciesException(Calculator calculator) {
        String calculators = calculatingCalculators.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        throw new IllegalStateException(String.format(CYCLE_DEPENDENCIES_MSG, calculator, calculators));
    }
}
