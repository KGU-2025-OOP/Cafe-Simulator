package graphics;

import entities.IGameObject;
import util.CoordSystem;
import util.Vector2f;
import util.ImageUtils;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Color;

public class TextBox implements IGameObject {
    protected Vector2f pos;
    protected float depth;
    protected StringBuffer text;
    protected String outStr;
    private StringBuffer textBak;
    private Font font;

    public TextBox(Vector2f pos, float depth, StringBuffer text, Font font) {
        this.pos = new Vector2f(pos);
        this.depth = depth;
        this.text = new StringBuffer(text);
        this.font = font;
        textBak = new StringBuffer();
        outStr = text.toString();
    }
    public StringBuffer getBufferHandle() {
        return text;
    }
    public Vector2f getPositionHandle() {
        return pos;
    }
    @Override
    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int width = g.getFontMetrics().stringWidth(outStr);
        g.drawString(outStr, (int)pos.x - width / 2, CoordSystem.getFlippedY(pos.y));
    }
    @Override
    public void update(float deltaTime) {
        if (textBak.compareTo(text) != 0) {
            outStr = text.toString();
            textBak.setLength(0);
            textBak.append(text);
        }
    }
    @Override
    public float getDepth() {
        return depth;
    }
}
