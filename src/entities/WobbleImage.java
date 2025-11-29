package entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

public class WobbleImage implements IGameObject {

    private float x, y;
    private float angleTime = 0f;
    private float depth;
    private Image img;

    // 개별 설정 가능한 변수들
    private float maxAngle = 30f; // 좌/우 기울기 각도 범위
    private float speed = 2f;     // 회전 속도
    private int size = 60;        // 이미지 크기

    public WobbleImage(float x, float y, String imagePath, float depth) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.img = Toolkit.getDefaultToolkit().getImage(imagePath);
        this.angleTime = (float)(Math.random() * Math.PI * 2);
        if (Math.random() < 0.5) this.maxAngle = -this.maxAngle;// 시작을 모두 다르게
    }

    @Override
    public void update(float deltaTime) {
        angleTime += deltaTime * speed;
    }

    @Override
    public void draw(Graphics2D g) {
        float angleDeg = (float)Math.sin(angleTime) * maxAngle;
        double angleRad = Math.toRadians(angleDeg);

        AffineTransform at = new AffineTransform();
        at.translate(x + size / 2, y + size / 2);
        at.rotate(angleRad);
        at.translate(-size / 2, -size / 2);
        at.scale((double)size / img.getWidth(null), (double)size / img.getHeight(null));

        g.drawImage(img, at, null);
    }

    @Override
    public float getDepth() {
        return depth;
    }

    // 외부에서 개별 설정하고 싶을 때
    public WobbleImage setSize(int size) {
        this.size = size;
        return this;
    }

    public WobbleImage setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public WobbleImage setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
        return this;
    }
}
