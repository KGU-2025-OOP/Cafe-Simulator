package entities;

import util.Vector2f;

import java.awt.Color;
import java.awt.Graphics2D;

public class MilkBox implements IGameObject {
    private Vector2f pos;
    private float speed;
    private float depth;
    private boolean stopped;
    private Cup targetCup;

    private int width = 20;
    private int height = 30;

    public MilkBox(Vector2f pos, float depth, Cup targetCup) {
        this.pos = pos;
        this.depth = depth;
        this.speed = 4.0f;
        this.stopped = false;
        this.targetCup = targetCup;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)pos.x, (int)pos.y, width, height);

        g.setColor(Color.GRAY);
        g.drawRect((int)pos.x, (int)pos.y, width, height);

        g.setColor(new Color(200, 200, 255));
        g.fillRect((int)pos.x + 3, (int)pos.y + 5, width - 6, 10);
    }

    @Override
    public void update(float deltaTime) {
        if (!stopped) {
            pos.y += speed * deltaTime;
            float targetY = targetCup.getPos().y;
            if (pos.y + height >= targetY + 20) {
                pos.y = targetY + 20 - height;
                stopped = true;
            }
        }
    }

    @Override
    public float getDepth() {
        return depth;
    }

    public Vector2f getPos() {
        return pos;
    }
}
