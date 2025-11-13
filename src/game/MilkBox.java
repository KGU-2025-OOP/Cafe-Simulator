package game;

import java.awt.Color;
import java.awt.Graphics2D;

public class MilkBox implements IGameObject {
    private float x, y;
    private float speed;
    private float depth;
    private boolean stopped;
    private Cup targetCup;

    private int width = 20;
    private int height = 30;

    public MilkBox(float startX, float startY, float depth, Cup targetCup) {
        this.x = startX;
        this.y = startY;
        this.depth = depth;
        this.speed = 4.0f;
        this.stopped = false;
        this.targetCup = targetCup;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, width, height);

        g.setColor(Color.GRAY);
        g.drawRect((int)x, (int)y, width, height);

        g.setColor(new Color(200, 200, 255));
        g.fillRect((int)x + 3, (int)y + 5, width - 6, 10);
    }

    @Override
    public void update() {
        if (!stopped) {
            y += speed;

            if (y + height >= targetCup.getY() + 20) {
                y = targetCup.getY() + 20 - height;
                stopped = true;
            }
        }
    }

    @Override
    public float getDepth() {
        return depth;
    }

    public float getY() { return y; }
    public float getX() { return x; }
}
