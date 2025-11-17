package graphics;

import entities.IGameObject;
import util.CoordSystem;
import util.ImageUtils;
import util.Vector2f;
import util.Vector2i;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.awt.geom.AffineTransform;

public class RenderQueue {
    final private PriorityQueue<IGameObject> queue;
    private BufferedImage buffer;
    private int width;
    private int height;
    public boolean shouldClear;
    // AffineTransform yFlip;

    public class PainterComparator implements Comparator<IGameObject> {
        public int compare(IGameObject go0, IGameObject go1) {
            float depth0 = go0.getDepth();
            float depth1 = go1.getDepth();

            if (depth0 < depth1) {
                return 1;
            }
            if (depth0 > depth1) {
                return -1;
            }
            return 0;
        }
    }

    public RenderQueue(int width, int height) {
        this.width = width;
        this.height = height;
        shouldClear = true;
        Comparator<IGameObject> painter = new PainterComparator();
        queue = new PriorityQueue<IGameObject>(painter);
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        CoordSystem.init(width, height);
        /*
        yFlip =  new AffineTransform();
        yFlip.translate(0, height);
        yFlip.scale(1, -1);
        */
        // left hand coordinate
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

        // g.setTransform(yFlip);

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
        CoordSystem.resize(width, height);
        /*
        yFlip.setToIdentity();
        yFlip.translate(0, height);
        yFlip.scale(1, -1);
         */
        clear();
        clearBuffer();
    }
}
