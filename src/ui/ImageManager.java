package ui;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    // ==========================================================
    // 이미지 경로 상수
    // ==========================================================
    // 배경 이미지
    // (기존 경로가 /ui/ 안에 있었다면 그것을 유지하세요. 여기선 /images/로 가정합니다.)
    public static final String IMG_START_BG = "/ui/background.jpg";
    public static final String IMG_SETUP_BG = "/ui/background.jpg";
    public static final String IMG_MAIN_BG  = "/ui/background.jpg";

    // [추가] 버튼 이미지 경로 (실제 파일 위치에 맞게 수정 필요!)
    public static final String BTN_CONTINUE = "/ui/image.png";
    public static final String BTN_NEW_GAME = "/ui/image.png";
    public static final String BTN_START    = "/ui/image.png";
    // public static final String BTN_EXIT     = "/images/btn_exit.png"; // 종료 버튼도 이미지로 할 경우 추가


    private static final Map<String, Image> imageCache = new HashMap<>();
    // [추가] ImageIcon 캐시 (버튼용)
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();


    // (기존) Image 객체 가져오기 (배경 그리기용)
    public static Image getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        URL imgUrl = ImageManager.class.getResource(path);
        if (imgUrl == null) {
            // System.err.println("[에러] 이미지를 찾을 수 없습니다: " + path);
            return null;
        }

        Image image = new ImageIcon(imgUrl).getImage();
        imageCache.put(path, image);
        return image;
    }

    // [추가] ImageIcon 객체 가져오기 (JButton용)
    public static ImageIcon getImageIcon(String path) {
        // 1. 캐시 확인
        if (iconCache.containsKey(path)) {
            return iconCache.get(path);
        }

        // 2. 리소스 로드
        URL imgUrl = ImageManager.class.getResource(path);
        if (imgUrl == null) {
            System.err.println("[에러] 버튼 이미지를 찾을 수 없습니다: " + path);
            // 이미지가 없을 때를 대비한 빈 아이콘 반환 (혹은 null)
            return new ImageIcon();
        }

        // 3. ImageIcon 생성 및 캐시 저장
        ImageIcon icon = new ImageIcon(imgUrl);
        iconCache.put(path, icon);
        return icon;
    }
}