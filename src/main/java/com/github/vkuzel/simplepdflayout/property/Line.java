package com.github.vkuzel.simplepdflayout.property;

import java.awt.*;

import static com.github.vkuzel.simplepdflayout.property.Line.Style.SOLID;
import static java.awt.Color.BLACK;

public final class Line {

    public enum Style {
        SOLID, DOTTED, DASHED, DASH_DOTTED
    }

    private final float width;
    private final Style style;
    private final Color color;

    private Line(float width, Style style, Color color) {
        this.width = width;
        this.style = style;
        this.color = color;
    }

    public static Line of() {
        return of(0, SOLID, BLACK);
    }

    public static Line of(float width, Style style, Color color) {
        return new Line(width, style, color);
    }

    public float getWidth() {
        return width;
    }

    public Line withWidth(float width) {
        return new Line(width, style, color);
    }

    public Style getStyle() {
        return style;
    }

    public Line withStyle(Style style) {
        return new Line(width, style, color);
    }

    public float[] calculatePattern() {
        switch (style) {
            case SOLID:
                return new float[]{};
            case DOTTED:
                return new float[]{1 * width, 1 * width};
            case DASHED:
                return new float[]{3 * width, 3 * width};
            case DASH_DOTTED:
                return new float[]{3 * width, 2 * width, 1 * width, 2 * width};
            default:
                throw new IllegalArgumentException("Unknown pattern " + style);
        }
    }

    public Color getColor() {
        return color;
    }

    public Line withColor(Color color) {
        return new Line(width, style, color);
    }
}
