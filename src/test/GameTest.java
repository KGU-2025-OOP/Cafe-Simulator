package test;

import core.GameCanvas;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class GameTest {
    public static void main(String[] args) {
        int width = 800;
        int height = 600;
        GameCanvas gameCanvas = new GameCanvas(width, height);

        Frame frame = new java.awt.Frame("Game window");
        frame.add(gameCanvas);
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gameCanvas.shouldRun = false;
            }
        });

        Thread gameThread = new Thread(gameCanvas);
        gameThread.start();
    }
}
