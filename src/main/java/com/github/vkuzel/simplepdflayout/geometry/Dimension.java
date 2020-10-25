package com.github.vkuzel.simplepdflayout.geometry;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public final class Dimension {

    private final float width;
    private final float height;

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Dimension(PDRectangle pdRectangle) {
        this.width = pdRectangle.getWidth();
        this.height = pdRectangle.getHeight();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "[" + width + ", " + height + ']';
    }
}
