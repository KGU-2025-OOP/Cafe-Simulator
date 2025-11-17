package util;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;

public class ImageUtils {
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage res = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = res.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return res;
    }
}
