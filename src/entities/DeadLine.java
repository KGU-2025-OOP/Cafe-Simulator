package entities;

import util.CoordSystem;
import util.Vector2f;

import java.awt.Graphics2D;
import java.awt.Color;

public class DeadLine implements IGameObject {
    private final int width;
    private float y;
    private final Vector2f move;
    private final float speed;


    public DeadLine(int width, float yPos, Vector2f move, float speed) {
        this.width = width;
        y = yPos;
        this.move = new Vector2f(move);
        this.speed = speed;
    }

    public boolean isDead(float yPos) {
        return y > yPos;
    }

    @Override
    public float getDepth() {
        return 0.F;
    }

    @Override
    public void update(float deltaTime) {
        y += move.y * deltaTime * speed;
    }

    public Vector2f getMoveHandle() {
        return move;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        int ty = CoordSystem.getFlippedY(y);
        g.drawLine(0, ty, width, ty);
    }
}
