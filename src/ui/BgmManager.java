package ui;

import javax.sound.sampled.*;
import java.net.URL;

public class BgmManager {

    private static final String BGM_PATH = "/image/background_music/Tomorrow's Light - Telecasted.wav";
    private static Clip clip;
    private static boolean on = false;

    public static void setOn(boolean turnOn) {
        on = turnOn;
        if (on) {
            play();
        } else {
            stop();
        }
    }

    public static boolean isOn() {
        return on;
    }

    private static void play() {
        try {
            if (clip == null) {
                URL url = BgmManager.class.getResource(BGM_PATH);
                if (url == null) return;

                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(ais);
            }

            if (!clip.isRunning()) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}