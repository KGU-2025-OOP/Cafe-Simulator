package entities;

import util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background implements IGameObject{
    private static final String IMAGE_PATH = "resources/image/background/game_background.jpg";
    private BufferedImage backgroundImage;
    public Background(int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(IMAGE_PATH));
            float ratio = image.getHeight() / (float)image.getWidth();
            backgroundImage = ImageUtils.resize(image, width, (int)(width * ratio));
        } catch (IOException e) {
            backgroundImage = null;
            assert(true);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(backgroundImage, null, 0, 0);
        // TODO: 분할 렌더링 필요 스프라이트가 너무커서 느려짐
    }

    @Override
    public void update(float deltaTime) {
        // Do nothing
    }

    @Override
    public float getDepth() {
        return 5.0F;
    }
}
