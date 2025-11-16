package util;

public class CoordSystem {
    private static int width = 0;
    private static int height = 0;

    public static void init(int width, int height) {
        resize(width, height);
    }

    public static void resize(int width, int height) {
        CoordSystem.width = width;
        CoordSystem.height = height;
    }

    public static int getFlippedY(float y) {
        return (int)(height - y);
    }

    public static void translateScreenCoord(Vector2f pos) {
        float x;
        float y;

        x = (pos.x + 1.0F) / 2;
        y = (1.0F - pos.y) / 2;
        x = x * width;
        y = y * height;

        pos.x = x;
        pos.y = y;
    }
}
