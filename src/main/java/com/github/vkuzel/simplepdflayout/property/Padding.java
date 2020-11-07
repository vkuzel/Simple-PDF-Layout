package com.github.vkuzel.simplepdflayout.property;

public final class Padding {

    private final float top;
    private final float right;
    private final float bottom;
    private final float left;

    private Padding(float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public static Padding of() {
        return of(0);
    }

    public static Padding of(float padding) {
        return of(padding, padding, padding, padding);
    }

    public static Padding of(float top, float right, float bottom, float left) {
        return new Padding(top, right, bottom, left);
    }

    public static Padding top(float top) {
        return of(top, 0, 0, 0);
    }

    public static Padding right(float right) {
        return of(0, right, 0, 0);
    }

    public static Padding bottom(float bottom) {
        return of(0, 0, bottom, 0);
    }

    public static Padding left(float left) {
        return of(0, 0, 0, left);
    }

    public float getTop() {
        return top;
    }

    public Padding withTop(float top) {
        return new Padding(top, right, bottom, left);
    }

    public float getRight() {
        return right;
    }

    public Padding withRight(float right) {
        return new Padding(top, right, bottom, left);
    }

    public float getBottom() {
        return bottom;
    }

    public Padding withBottom(float bottom) {
        return new Padding(top, right, bottom, left);
    }

    public float getLeft() {
        return left;
    }

    public Padding withLeft(float left) {
        return new Padding(top, right, bottom, left);
    }
}
