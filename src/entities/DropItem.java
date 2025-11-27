package entities;

import graphics.TextBox;
import util.CoordSystem;
import util.ImageUtils;
import util.Vector2f;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class DropItem extends TextBox {
    private boolean isValid;
    private final Vector2f move;
    private final Vector2f adder;
    private final float speed;
    private final DeadLine deadLine;
    private BufferedImage background;
    public static Font defaultFont = new Font("Batang", Font.ITALIC, 24);
    private float height;

    public DropItem(Vector2f pos, Vector2f move, float speed, String str, Font font, BufferedImage background, DeadLine deadLine) {
        super(new Vector2f(pos), 1.0F, new StringBuffer(str), font);
        height = pos.y;
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
        if (deadLine != null && deadLine.isDead(pos.y)) {
            pos.y = CoordSystem.getFlippedY(height);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isValid) {
            if (background != null) {
                g.drawImage(background, null, (int)pos.x - background.getWidth() / 2, CoordSystem.getFlippedY(pos.y + (float) background.getHeight() / 2));
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

    public BufferedImage getBackgroundHandle() {
        return background;
    }

    public boolean shouldRemove() {
        return isValid;
    }

    public Vector2f getMoveHandle() {
        return move;
    }

    public void resize(float imgSize) {
        int height = background.getHeight();
        int width = background.getWidth();
        float ratio = height / (float)width;
        background = ImageUtils.resize(background, (int)imgSize, (int)(imgSize * ratio));
    }
}
