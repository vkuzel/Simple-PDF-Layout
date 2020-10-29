package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.ParentElement;
import com.github.vkuzel.simplepdflayout.property.XPosition;
import com.github.vkuzel.simplepdflayout.property.YPosition;

import java.util.LinkedHashSet;
import java.util.Set;

public final class RelativeToElementPositionCalculator implements PositionCalculator {

    private final ParentElement<?> parentElement;
    private final Element element;
    private final XPosition xPosition;
    private final YPosition yPosition;
    private final Element positionElement;

    public RelativeToElementPositionCalculator(ParentElement<?> parentElement, Element element, XPosition xPosition, YPosition yPosition, Element positionElement) {
        this.parentElement = parentElement;
        this.element = element;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.positionElement = positionElement;
    }

    @Override
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        if (positionElement != null) {
            return calculateFromPositionElement(calculatorPath);
        } else {
            return calculateFromParentElement(calculatorPath);
        }
    }

    private float calculateFromPositionElement(Set<Calculator> calculatorPath) {
        float position;
        if (xPosition != null) {
            switch (xPosition) {
                case TO_LEFT:
                    position = positionElement.calculateX(new LinkedHashSet<>(calculatorPath));
                    position -= element.calculateWidth(new LinkedHashSet<>(calculatorPath));
                    break;
                case TO_RIGHT:
                    position = positionElement.calculateX(new LinkedHashSet<>(calculatorPath));
                    position += positionElement.calculateWidth(new LinkedHashSet<>(calculatorPath));
                    break;
                case OVERLAPS_FROM_LEFT:
                    position = positionElement.calculateX(calculatorPath);
                    break;
                default:
                    throw new IllegalStateException("Unsupported XPosition " + xPosition);
            }
        } else if (yPosition != null) {
            switch (yPosition) {
                case TO_TOP:
                    position = positionElement.calculateY(new LinkedHashSet<>(calculatorPath));
                    position -= element.calculateHeight(new LinkedHashSet<>(calculatorPath));
                    break;
                case TO_BOTTOM:
                    position = positionElement.calculateY(new LinkedHashSet<>(calculatorPath));
                    position += positionElement.calculateHeight(new LinkedHashSet<>(calculatorPath));
                    break;
                case OVERLAPS_FROM_TOP:
                    position = positionElement.calculateY(calculatorPath);
                    break;
                default:
                    throw new IllegalStateException("Unsupported YPosition " + yPosition);
            }
        } else {
            throw new IllegalStateException("Position is not set!");
        }
        return position;
    }

    private float calculateFromParentElement(Set<Calculator> calculatorPath) {
        if (xPosition != null) {
            return parentElement.calculateContentX(calculatorPath);
        } else if (yPosition != null) {
            return parentElement.calculateContentY(calculatorPath);
        } else {
            throw new IllegalStateException("Position is not set!");
        }
    }

    @Override
    public String toString() {
        return "RelativeToElementPositionCalculator@" + Integer.toHexString(hashCode()) + "{" +
                "parentElement=" + parentElement +
                ", element=" + element +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", positionElement=" + positionElement +
                '}';
    }
}
