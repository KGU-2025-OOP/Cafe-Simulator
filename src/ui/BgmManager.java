package ui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BgmManager {

    private static final String BGM_PATH = "/image/background_music/Tomorrow's Light - Telecasted.wav";

    private static Clip clip;
    private static boolean on = false;

    // 내부에서만 쓰는: 실제로 로딩 & 루프 재생
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

            // 이미 재생 중이면 다시 시작하지 않기 (패널 바뀐다고 리스타트 안 되도록)
            if (!clip.isRunning()) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);  // 계속 반복 재생
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

    // 외부에서 사용할 ON/OFF 함수
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
