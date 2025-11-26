package ui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BgmManager {

    private static final String BGM_PATH = "/image/background_music/Tomorrow's Light - Telecasted.wav";

    private static Clip clip;
    private static boolean on = false;

    private static void startLoopInternal() {
        try {
            if (clip == null) {
                URL url = BgmManager.class.getResource(BGM_PATH);
                if (url == null) {
                    System.err.println("BGM 파일을 찾을 수 없습니다: " + BGM_PATH);
                    return;
                }

                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(ais);
            }

            if (!clip.isRunning()) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static void stopInternal() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void setOn(boolean turnOn) {
        on = turnOn;
        if (on) {
            startLoopInternal();
        } else {
            stopInternal();
        }
    }

    public static boolean isOn() {
        return on;
    }
}
