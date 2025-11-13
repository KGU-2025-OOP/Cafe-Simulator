
import java.awt.Color;
import java.awt.Graphics2D;

public class Cup implements IGameObject {

    public Cup(float x, float y, float depth) {
        this.x = x;
        this.y = y;
        this.depth = depth;
    }

    @Override
    public void draw(Graphics2D g) {
        // 컵 바디
        g.setColor(Color.WHITE);
        g.fillRoundRect((int)x, (int)y, 60, 80, 10, 10);

        // 컵 테두리
        g.setColor(Color.GRAY);
        g.drawRoundRect((int)x, (int)y, 60, 80, 10, 10);

        // 손잡이
        g.setColor(Color.LIGHT_GRAY);
        g.drawArc((int)(x + 50), (int)(y + 20), 20, 40, 270, 180);

        // 컵 안쪽 그림자 (커피 자리)
        g.setColor(new Color(150, 75, 0));
        g.fillOval((int)(x + 5), (int)(y + 5), 50, 30);

        // 반사광
        g.setColor(new Color(255, 255, 255, 80));
        g.fillOval((int)(x + 10), (int)(y + 10), 15, 10);
    }

    @Override
    public void update() {
        // 컵은 고정되어 움직이지 않음
    }

    @Override
    public float getDepth() {
        return depth;
    }

    // 다른 오브젝트가 컵 위치 확인용
    public float getY() { return y; }
    public float getX() { return x; }
}
