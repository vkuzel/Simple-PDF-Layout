package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.property.XPosition;
import com.github.vkuzel.simplepdflayout.property.YPosition;

import java.util.HashSet;
import java.util.Set;

public final class RelativeToElementPositionCalculator implements PositionCalculator {

    private final Element element;
    private final XPosition xPosition;
    private final YPosition yPosition;
    private final Element positionElement;

    public RelativeToElementPositionCalculator(Element element, XPosition xPosition, YPosition yPosition, Element positionElement) {
        this.element = element;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.positionElement = positionElement;
    }

    @Override
    public float calculate(Set<Calculator> calculatorPath) {
        validatePath(calculatorPath);
        float position;
        if (xPosition != null) {
            switch (xPosition) {
                case TO_LEFT:
                    position = positionElement.calculateX(new HashSet<>(calculatorPath));
                    position -= element.calculateWidth(new HashSet<>(calculatorPath));
                    break;
                case TO_RIGHT:
                    position = positionElement.calculateX(new HashSet<>(calculatorPath));
                    position += positionElement.calculateWidth(new HashSet<>(calculatorPath));
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
                    position = positionElement.calculateY(new HashSet<>(calculatorPath));
                    position -= element.calculateHeight(new HashSet<>(calculatorPath));
                    break;
                case TO_BOTTOM:
                    position = positionElement.calculateY(new HashSet<>(calculatorPath));
                    position += positionElement.calculateHeight(new HashSet<>(calculatorPath));
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
}
