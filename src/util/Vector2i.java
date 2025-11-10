package util;

public class Vector2i {
    public int x;
    public int y;

    public Vector2i() {
        x = 0;
        y = 0;
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(final Vector2i src) {
        x = src.x;
        y = src.y;
    }
    public void add(final Vector2i rhs) {
        x += rhs.x;
        y += rhs.y;
    }

    public void sub(final Vector2i rhs) {
        x -= rhs.x;
        y -= rhs.y;
    }

    public int dot(final Vector2i rhs) {
        return (x * rhs.x) + (y * rhs.y);
    }

    public float length() {
        return (float)Math.sqrt(dot(this));
    }
}
