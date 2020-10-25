package com.github.vkuzel.simplepdflayout.geometry;

public final class Point {

    private final float x;
    private final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distance(Point point) {
        float nx = x - point.x;
        float ny = y - point.y;
        return (float) Math.sqrt(nx * nx + ny * ny);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ']';
    }
}
