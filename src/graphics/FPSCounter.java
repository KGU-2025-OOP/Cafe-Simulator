package graphics;

public class FPSCounter {
    public static final long secondInNanoTime = 1000000000;
    private long timer;
    private long lastTime;
    private int frames;
    private int fps;

    public FPSCounter() {
        timer = lastTime = System.nanoTime();

        frames = 0;
        fps = 0;
    }

    public void printFPS() {
        ++frames;
        long currentTime = System.nanoTime();
        if (currentTime - timer >= secondInNanoTime) {
            fps = frames;
            frames = 0;
            timer = currentTime;
            System.out.println("FPS: " + fps);
        }
    }

    public void limitFPS(int fps) {
        long frameTime = secondInNanoTime / fps;
        long currentTime = System.nanoTime();
        while (currentTime - lastTime < frameTime) {

            currentTime = System.nanoTime();
        }
        lastTime = System.nanoTime();
    }
}
