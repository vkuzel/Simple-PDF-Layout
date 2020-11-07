package com.github.vkuzel.simplepdflayout.property;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import static org.apache.pdfbox.pdmodel.common.PDRectangle.A4;

public final class Dimension {

    private final float width;
    private final float height;

    private Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public static Dimension of(float width, float height) {
        return new Dimension(width, height);
    }

    public static Dimension a4() {
        return ofPdRectangle(A4);
    }

    public static Dimension ofPdRectangle(PDRectangle pdRectangle) {
        return of(pdRectangle.getWidth(), pdRectangle.getHeight());
    }

    public PDRectangle toPdRectangle() {
        return new PDRectangle(width, height);
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
