package com.github.vkuzel.simplepdflayout.geometry;

public class Dimension {

    private final float width;
    private final float height;

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
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
