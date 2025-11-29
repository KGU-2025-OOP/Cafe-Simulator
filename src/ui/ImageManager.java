package ui;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    public static final String IMG_START_BG = "/image/background/background_2.jpg";
    public static final String IMG_SETUP_BG = "/image/background/background_2.jpg";
    public static final String IMG_MAIN_BG  = "/image/background/background_gameplay_area.jpg";
    public static final String IMG_MENU_BG  = "/image/background/background_gameplay_area.jpg";

    public static final String BTN_CONTINUE = "/image/button/play_button.png";
    public static final String BTN_NEW_GAME = "/image/button/start_button.png";
    public static final String BTN_START    = "/image/button/start_button.png";
    public static final String BTN_EXIT     = "/image/button/exit_button.png";
    public static final String BTN_DEFAULT  = "/image/button/default_button.png";
    public static final String BTN_MINI     = "/image/button/mini_button.png";
    public static final String BTN_BACK     = "/image/button/back_button.png";
    public static final String BTN_MENU_TOGGLE = "/image/button/menu_toggle_button.png";

    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();

    public static Image getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        URL imgUrl = ImageManager.class.getResource(path);
        if (imgUrl == null) {
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
            return new ImageIcon();
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        iconCache.put(path, icon);
        return icon;
    }
}