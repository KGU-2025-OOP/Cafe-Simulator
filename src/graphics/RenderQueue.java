package graphics;

import game.IGameObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.PriorityQueue;

public class RenderQueue {
    final private PriorityQueue<IGameObject> queue;
    private BufferedImage buffer;
    private int width;
    private int height;
    public boolean shouldClear;

    public RenderQueue(int width, int height) {
        this.width = width;
        this.height = height;
        shouldClear = true;
        Comparator<IGameObject> painter = (IGameObject o0, IGameObject o1) -> Float.compare(o0.getDepth(), o1.getDepth());
        queue = new PriorityQueue<IGameObject>(painter);
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void setScreenResetEnabled(boolean enabled) {
        shouldClear = enabled;
    }

    public void add(IGameObject object) {
        queue.add(object);
    }

    public void clear() {
        queue.clear();
    }

    public void clearBuffer() {
        Graphics g = buffer.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
    }

    public BufferedImage flush() {
        Graphics2D g = buffer.createGraphics();
        g.setClip(0, 0, width, height);
        if (shouldClear) {
            clearBuffer();
        }
        while (!queue.isEmpty()) {
            queue.poll().draw(g);
        }
        g.dispose();
        return buffer;
    }
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new BufferedImage(width, height,  BufferedImage.TYPE_INT_ARGB);
        clear();
        clearBuffer();
    }
}
