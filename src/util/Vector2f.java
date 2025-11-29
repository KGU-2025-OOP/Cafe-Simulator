package util;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
        x = 0;
        y = 0;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(final Vector2f src) {
        x = src.x;
        y = src.y;
    }

    public Vector2f(float angle) {
        this();
        this.setVectorByAngle(angle);
    }

    public void setVectorByAngle(float angle) {
        x = (float)Math.cos(angle);
        y = (float)Math.sin(angle);
    }

    public void rotate(float angle) {
        float newX = x * (float)Math.cos(angle) - y * (float)Math.sin(angle);
        float newY = x * (float)Math.sin(angle) + y * (float)Math.cos(angle);
        x = newX;
        y = newY;
    }

    public void add(final Vector2f rhs) {
        x += rhs.x;
        y += rhs.y;
    }

    public void sub(final Vector2f rhs) {
        x -= rhs.x;
        y -= rhs.y;
    }

    public float dot(final Vector2f rhs) {
        return (x * rhs.x) + (y * rhs.y);
    }

    public float getLength() {
        return (float) Math.sqrt(dot(this));
    }

    public void normalize() {
        float length = getLength();
        x = x / length;
        y = y / length;
    }

    public void scale(float s) {
        x = s * x;
        y = s * y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}