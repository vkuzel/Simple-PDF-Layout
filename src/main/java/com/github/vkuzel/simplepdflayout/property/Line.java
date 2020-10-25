package com.github.vkuzel.simplepdflayout.property;

import java.awt.*;

public final class Line {

    public enum Style {
        SOLID, DOTTED, DASHED, DASH_DOTTED
    }

    private float width = 0;
    private Style style = Style.SOLID;
    private Color color = Color.BLACK;

    public float getWidth() {
        return width;
    }

    public Line setWidth(float width) {
        this.width = width;
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public Line setStyle(Style style) {
        this.style = style;
        return this;
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

    public Line setColor(Color color) {
        this.color = color;
        return this;
    }
}
