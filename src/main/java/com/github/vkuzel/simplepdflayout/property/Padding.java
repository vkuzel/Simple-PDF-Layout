package com.github.vkuzel.simplepdflayout.property;

public final class Padding {

    private final float top;
    private final float right;
    private final float bottom;
    private final float left;

    public Padding() {
        this(0, 0, 0, 0);
    }

    public Padding(float padding) {
        this(padding, padding, padding, padding);
    }

    public Padding(float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public float getLeft() {
        return left;
    }
}
