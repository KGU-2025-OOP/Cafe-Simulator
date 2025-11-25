package entities;

import graphics.TextBox;
import util.CoordSystem;
import util.Vector2f;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class DropItem extends TextBox {
    private boolean isValid;
    private Vector2f move;
    private Vector2f adder;
    private float speed;
    private DeadLine deadLine;
    private BufferedImage background;

    public DropItem(Vector2f pos, Vector2f move, float speed, String str, Font font, BufferedImage background, DeadLine deadLine) {
        super(new Vector2f(pos), 1.0F, new StringBuffer(str), font);
        this.move = new Vector2f(move);
        adder = new Vector2f();
        this.deadLine = deadLine;
        this.speed = speed;
        isValid = true;
        this.background = background;
    }

    @Override
    public void update(float deltaTime) {
        adder.x = move.x;
        adder.y = move.y;
        adder.scale(deltaTime * speed);
        pos.add(adder);
        if (deadLine.isDead(pos.y)) {
            isValid = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isValid) {
            if (background != null) {
                g.drawImage(background, null, (int)pos.x - background.getWidth() / 2, CoordSystem.getFlippedY(pos.y - background.getHeight() / 2));
            }
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

    public Vector2f getMoveHandle() {
        return move;
    }
}
