package com.github.vkuzel.simplepdflayout.geometry;

public final class Vector {

    private final float x;
    private final float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector ofLineSegment(Point start, Point end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY());
    }

    public Vector normalize() {
        float l = length();
        return new Vector(x / l, y / l);
    }

    public Vector scale(float length) {
        Vector n = normalize();
        return new Vector(n.x * length, n.y * length);
    }

    public Vector normal() {
        return new Vector(y, -x);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
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
