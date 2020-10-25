package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

import java.util.HashSet;
import java.util.Set;

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
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        float parentContentPosition;
        float parentContentDimension;
        switch (axis) {
            case X:
                parentContentPosition = parentElement.calculateContentX(new HashSet<>(calculatorPath));
                parentContentDimension = parentElement.calculateContentWidth(new HashSet<>(calculatorPath));
                break;
            case Y:
                parentContentPosition = parentElement.calculateContentY(new HashSet<>(calculatorPath));
                parentContentDimension = parentElement.calculateContentHeight(new HashSet<>(calculatorPath));
                break;
            default:
                throw new IllegalStateException("Unsupported axis " + axis);
        }
        return parentContentPosition + positionPercent * parentContentDimension / 100;
    }
}
