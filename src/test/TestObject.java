package test;

import game.IGameObject;
import util.Vector2i;

import java.awt.*;

public class TestObject implements IGameObject {
    Vector2i pos;
    int width;

    public TestObject(int width, int height) {
        this.width = width;
        pos = new Vector2i(0, height / 2);
    }

    @Override
    public float getDepth() {
        return 1.0F;
    }

    @Override
    public void update() {
        pos.x = (pos.x + 1) % width;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(pos.x, pos.y, 30, 30);
    }
}
