package com.github.vkuzel.simplepdflayout.property;

public final class Border {

    private final Line top;
    private final Line right;
    private final Line bottom;
    private final Line left;

    public Border() {
        this(new Line(), new Line(), new Line(), new Line());
    }

    public Border(Line border) {
        this(border, border, border, border);
    }

    public Border(Line top, Line right, Line bottom, Line left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public Line getTop() {
        return top;
    }

    public Line getRight() {
        return right;
    }

    public Line getBottom() {
        return bottom;
    }

    public Line getLeft() {
        return left;
    }
}
