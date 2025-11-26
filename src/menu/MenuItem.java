package menu;

import util.ImageUtils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
/**
 * 재료와 옵션의 공통 속성과 동작을 정의하는 추상 클래스
 */
public abstract class MenuItem {
    protected String name;
    protected int price;
    protected String imgPath; //이미지 경로
    protected java.io.File imagePath; //이미지 파일
    protected BufferedImage image;
    public static int imageSize = 45;

    public MenuItem(Scanner scan) {
        String line = scan.nextLine();
        String[] parts = line.split(" ");

        this.name = parts[0];
        this.price = Integer.parseInt(parts[1]);
        if (parts.length > 2) {
            this.imgPath = parts[2];
            // 이미지 파일 로드 ===========================================
            java.io.File file = new java.io.File(this.imgPath);
            if (file.exists()) { // 이미지 파일이 존재하는지 확인
                this.imagePath = file;
                try {
                    image = ImageIO.read(file);
                    float ratio = image.getHeight() / (float)image.getWidth();
                    image = ImageUtils.resize(image, imageSize, (int)Math.ceil(imageSize * ratio));
                } catch (IOException e) {
                    image = null;
                }
            } else {
                image = null;
                //throw new IllegalArgumentException("Image file not found: " + this.imgPath);
            } // 이미지 파일 로드 ===========================================
        }
    }

    public MenuItem matches(String type, String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }

    // Getter 메서드들
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImgPath() {
        return imgPath;
    }
    public BufferedImage getBufferedImage() {
        return image;
    }
}
