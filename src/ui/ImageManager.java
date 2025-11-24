package ui;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    // ==========================================================
    // [수정완료] resources 폴더가 루트이므로, 경로에서 '/resources'를 뺐습니다.
    // ==========================================================

    // 1. 배경 이미지 (실제 위치: resources/image/background/background_2.jpg)
    // 코드 경로: /image/background/background_2.jpg
    public static final String IMG_START_BG = "/image/background/background_2.jpg";
    public static final String IMG_SETUP_BG = "/image/background/background_2.jpg";
    public static final String IMG_MAIN_BG  = "/image/background/background_2.jpg";

    // 2. 버튼 이미지 (실제 위치: resources/image/button/...)
    // 코드 경로: /image/button/...
    public static final String BTN_CONTINUE = "/image/button/play_button.png";
    public static final String BTN_NEW_GAME = "/image/button/play_button.png";
    public static final String BTN_START    = "/image/button/play_button.png";
    public static final String BTN_EXIT     = "/image/button/exit_button.png";


    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();

    public static Image getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        // 리소스 로드
        URL imgUrl = ImageManager.class.getResource(path);

        if (imgUrl == null) {
            System.err.println("[에러] 이미지를 찾을 수 없습니다: " + path);
            System.err.println(" -> [해결법] resources 폴더 우클릭 -> 'Mark Directory as' -> 'Resources Root' 선택");
            System.err.println(" -> 그 후 [Build] -> [Rebuild Project] 실행");
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