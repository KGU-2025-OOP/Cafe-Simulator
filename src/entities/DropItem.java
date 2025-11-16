package entities;

import graphics.TextBox;
import util.Vector2f;

import java.awt.Graphics2D;
import java.awt.Font;

public class DropItem extends TextBox {
    private boolean isValid;
    private Vector2f move;
    private float speed;
    private DeadLine deadLine;

    public DropItem(Vector2f pos, Vector2f move, float speed, String str, Font font, DeadLine deadLine) {
        super(new Vector2f(pos), 1.0F, new StringBuffer(str), font);
        this.move = new Vector2f(move);
        this.deadLine = deadLine;
        this.speed = speed;
        isValid = true;
    }

    @Override
    public void update(float deltaTime) {
        // super.update(deltaTime);
        move.scale(deltaTime * speed);
        pos.add(move);
        if (deadLine.isDead(pos.y)) {
            isValid = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isValid) {
            super.draw(g);
        }
    }

    public boolean match(String str) {
        super.update(0);
        if (outStr.equals(str)) {
            isValid = false;
            return true;
        }
        return false;
    }

    public boolean shouldRemove() {
        return isValid;
    }

}
