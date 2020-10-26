package com.github.vkuzel.simplepdflayout.property;

public final class Border {

    private final Line top;
    private final Line right;
    private final Line bottom;
    private final Line left;

    private Border(Line top, Line right, Line bottom, Line left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public static Border of() {
        return of(Line.of());
    }

    public static Border of(Line border) {
        return of(border, border, border, border);
    }

    public static Border of(Line top, Line right, Line bottom, Line left) {
        return new Border(top, right, bottom, left);
    }

    public static Border top(Line top) {
        Line line = Line.of();
        return of(top, line, line, line);
    }

    public static Border right(Line right) {
        Line line = Line.of();
        return of(line, right, line, line);
    }

    public static Border bottom(Line bottom) {
        Line line = Line.of();
        return of(line, line, bottom, line);
    }

    public static Border left(Line left) {
        Line line = Line.of();
        return of(line, line, line, left);
    }

    public Line getTop() {
        return top;
    }

    public Border withTop(Line top) {
        return new Border(top, right, bottom, left);
    }

    public Line getRight() {
        return right;
    }

    public Border withRight(Line right) {
        return new Border(top, right, bottom, left);
    }

    public Line getBottom() {
        return bottom;
    }

    public Border withBottom(Line bottom) {
        return new Border(top, right, bottom, left);
    }

    public Line getLeft() {
        return left;
    }

    public Border withLeft(Line left) {
        return new Border(top, right, bottom, left);
    }
}
