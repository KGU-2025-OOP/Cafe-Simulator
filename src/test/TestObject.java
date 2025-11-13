package test;

import game.IGameObject;
import util.Vector2i;
import util.Vector2f;

import java.awt.Graphics2D;
import java.awt.Color;

public class TestObject implements IGameObject {
    Vector2f pos;
    int width;
    int height;
    float depth;
    Vector2f displacement;


    public TestObject(int width, int height, float depth, Vector2f displacement) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        pos = new Vector2f(width / 2, height / 2);
        this.displacement = displacement;

    }

    @Override
    public float getDepth() {
        return depth;
    }

    @Override
    public void update(float deltaTime) {
        Vector2f move = new Vector2f(displacement);
        move.scale(deltaTime);
        pos.add(move);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)pos.x, (int)pos.y, 30, 30);
    }
}
