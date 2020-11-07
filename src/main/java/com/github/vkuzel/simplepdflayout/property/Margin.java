package com.github.vkuzel.simplepdflayout.property;

public final class Margin {

    private final float top;
    private final float right;
    private final float bottom;
    private final float left;

    private Margin(float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public static Margin of() {
        return of(0);
    }

    public static Margin of(float margin) {
        return of(margin, margin, margin, margin);
    }

    public static Margin of(float top, float right, float bottom, float left) {
        return new Margin(top, right, bottom, left);
    }

    public static Margin top(float top) {
        return of(top, 0, 0, 0);
    }

    public static Margin right(float right) {
        return of(0, right, 0, 0);
    }

    public static Margin bottom(float bottom) {
        return of(0, 0, bottom, 0);
    }

    public static Margin left(float left) {
        return of(0, 0, 0, left);
    }

    public float getTop() {
        return top;
    }

    public Margin withTop(float top) {
        return new Margin(top, right, bottom, left);
    }

    public float getRight() {
        return right;
    }

    public Margin withRight(float right) {
        return new Margin(top, right, bottom, left);
    }

    public float getBottom() {
        return bottom;
    }

    public Margin withBottom(float bottom) {
        return new Margin(top, right, bottom, left);
    }

    public float getLeft() {
        return left;
    }

    public Margin withLeft(float left) {
        return new Margin(top, right, bottom, left);
    }
}
