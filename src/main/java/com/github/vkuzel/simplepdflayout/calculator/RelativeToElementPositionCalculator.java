package com.github.vkuzel.simplepdflayout.calculator;

import com.github.vkuzel.simplepdflayout.Element;
import com.github.vkuzel.simplepdflayout.ParentElement;
import com.github.vkuzel.simplepdflayout.property.XPosition;
import com.github.vkuzel.simplepdflayout.property.YPosition;

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
    public float calculate(CalculationContext calculationContext) {
        return calculationContext.compute(this, this::calculateInternal);
    }

    private float calculateInternal(CalculationContext calculationContext) {
        if (positionElement != null) {
            return calculateFromPositionElement(calculationContext);
        } else {
            return calculateFromParentElement(calculationContext);
        }
    }

    private float calculateFromPositionElement(CalculationContext calculationContext) {
        float position;
        if (xPosition != null) {
            switch (xPosition) {
                case TO_LEFT:
                    position = positionElement.calculateX(calculationContext);
                    position -= element.calculateWidth(calculationContext);
                    break;
                case TO_RIGHT:
                    position = positionElement.calculateX(calculationContext);
                    position += positionElement.calculateWidth(calculationContext);
                    break;
                case OVERLAPS_FROM_LEFT:
                    position = positionElement.calculateX(calculationContext);
                    break;
                default:
                    throw new IllegalStateException("Unsupported XPosition " + xPosition);
            }
        } else if (yPosition != null) {
            switch (yPosition) {
                case TO_TOP:
                    position = positionElement.calculateY(calculationContext);
                    position -= element.calculateHeight(calculationContext);
                    break;
                case TO_BOTTOM:
                    position = positionElement.calculateY(calculationContext);
                    position += positionElement.calculateHeight(calculationContext);
                    break;
                case OVERLAPS_FROM_TOP:
                    position = positionElement.calculateY(calculationContext);
                    break;
                default:
                    throw new IllegalStateException("Unsupported YPosition " + yPosition);
            }
        } else {
            throw new IllegalStateException("Position is not set!");
        }
        return position;
    }

    private float calculateFromParentElement(CalculationContext calculationContext) {
        if (xPosition != null) {
            return parentElement.calculateContentX(calculationContext);
        } else if (yPosition != null) {
            return parentElement.calculateContentY(calculationContext);
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
