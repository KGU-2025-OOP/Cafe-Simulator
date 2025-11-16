package entities;

import util.ImageUtils;
import util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cup implements IGameObject {
    private Vector2f pos;
    private float depth;
    private static final Color coffee = new Color(150, 75, 0);
    private BufferedImage coffeeSprite;

    public Cup(Vector2f pos, float depth) throws IOException {
        this.pos = pos;
        this.depth = depth;
        coffeeSprite = ImageIO.read(new File("resources/iced_coffee.png"));
        coffeeSprite = ImageUtils.resize(coffeeSprite, coffeeSprite.getWidth() / 10, coffeeSprite.getHeight() / 10);
    }

    @Override
    public void draw(Graphics2D g) {
        /*
        // 컵 바디
        g.setColor(Color.WHITE);
        g.fillRoundRect((int)pos.x, (int)pos.y, 60, 80, 10, 10);

        // 컵 테두리
        g.setColor(Color.GRAY);
        g.drawRoundRect((int)pos.x, (int)pos.y, 60, 80, 10, 10);

        // 손잡이
        g.setColor(Color.LIGHT_GRAY);
        g.drawArc((int)(pos.x + 50), (int)(pos.y + 20), 20, 40, 270, 180);

        // 컵 안쪽 그림자 (커피 자리)
        g.setColor(coffee);
        g.fillOval((int)(pos.x + 5), (int)(pos.y + 5), 50, 30);

        // 반사광
        g.setColor(new Color(255, 255, 255, 80));
        g.fillOval((int)(pos.x + 10), (int)(pos.y + 10), 15, 10);
        */
        AffineTransform old = g.getTransform();
        float centerX = coffeeSprite.getWidth() / 2.F + pos.x;
        float centerY = coffeeSprite.getHeight() / 2.F + pos.y;

        g.rotate(Math.toRadians(30), centerX, centerY);
        g.drawImage(coffeeSprite, (int)pos.x - coffeeSprite.getWidth() / 2, (int)pos.y - coffeeSprite.getHeight() / 2, null);
        g.setTransform(old);
    }

    @Override
    public void update(float deltaTime) {
        // 컵은 고정되어 움직이지 않음
    }

    @Override
    public float getDepth() {
        return depth;
    }

    // 다른 오브젝트가 컵 위치 확인용
     public Vector2f getPos() {
        return pos;
     }
}
