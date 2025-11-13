package graphics;

public class FPSCounter {
    public static final long secondInNanoTime = 1000000000;
    private long lastTime;
    private int frames;
    private int fps;

    public FPSCounter() {
        lastTime = System.nanoTime();
        frames = 0;
        fps = 0;
    }

    public void printFPS() {
        ++frames;
        long currentTime = System.nanoTime();
        if (currentTime - lastTime >= secondInNanoTime) {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
            System.out.println("FPS: " + fps);
        }
    }
}
