package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.ParentElement;

import java.util.LinkedHashSet;
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
                parentContentPosition = parentElement.calculateContentX(new LinkedHashSet<>(calculatorPath));
                parentContentDimension = parentElement.calculateContentWidth(new LinkedHashSet<>(calculatorPath));
                break;
            case Y:
                parentContentPosition = parentElement.calculateContentY(new LinkedHashSet<>(calculatorPath));
                parentContentDimension = parentElement.calculateContentHeight(new LinkedHashSet<>(calculatorPath));
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
