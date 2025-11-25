package ui;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    // ==========================================================
    // 이미지 경로 설정
    // ==========================================================

    // 1. 배경 이미지
    public static final String IMG_START_BG = "/image/background/background_2.jpg";
    public static final String IMG_SETUP_BG = "/image/background/background_2.jpg";
    public static final String IMG_MAIN_BG  = "/image/background/background_GamePlayArea.jpg";

    // [추가] 메뉴 도감 배경 이미지 (파일명: background_GamePlayArea.jpg)
    public static final String IMG_MENU_BG  = "/image/background/background_GamePlayArea.jpg";

    // 2. 버튼 이미지
    public static final String BTN_CONTINUE = "/image/button/play_button.png";
    public static final String BTN_NEW_GAME = "/image/button/play_button.png";
    public static final String BTN_START    = "/image/button/play_button.png";
    public static final String BTN_EXIT     = "/image/button/exit_button.png";
    public static final String BTN_DEFAULT  = "/image/button/default_button.png";
    public static final String BTN_MINI     = "/image/button/mini_button.png";


    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();

    public static Image getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        URL imgUrl = ImageManager.class.getResource(path);

        if (imgUrl == null) {
            System.err.println("[에러] 이미지를 찾을 수 없습니다: " + path);
            return null;
        }

        Image image = new ImageIcon(imgUrl).getImage();
        imageCache.put(path, image);
        return image;
    }

    public static ImageIcon getImageIcon(String path) {
        if (iconCache.containsKey(path)) {
            return iconCache.get(path);
        }

        URL imgUrl = ImageManager.class.getResource(path);

        if (imgUrl == null) {
            System.err.println("[에러] 버튼 아이콘을 찾을 수 없습니다: " + path);
            return new ImageIcon();
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        iconCache.put(path, icon);
        return icon;
    }
}