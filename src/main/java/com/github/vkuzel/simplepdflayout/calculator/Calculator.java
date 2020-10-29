package com.github.vkuzel.simplepdflayout.calculator;

import java.util.Set;
import java.util.stream.Collectors;

public interface Calculator {

    float calculate(Set<Calculator> calculatorPath);

    default void validatePath(Set<Calculator> calculatorPath) {
        if (!calculatorPath.add(this)) {
            String calculators = calculatorPath.stream().map(Object::toString).collect(Collectors.joining(" -> "));
            throw new IllegalStateException("Circular calculator dependency! Calculators: " + calculators);
        } else if (calculatorPath.size() > 100) {
            String calculators = calculatorPath.stream().map(Object::toString).collect(Collectors.joining(" -> "));
            throw new IllegalStateException("Too long calculator path! Calculators: " + calculators);
        }
    }
}
