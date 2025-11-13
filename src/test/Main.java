package test;

import game.GameFrame;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class Main {
    public static void main(String[] args) {
        int width = 800;
        int height = 600;
        GameFrame gameFrame = new GameFrame(width, height);

        Frame frame = new java.awt.Frame("Game window");
        frame.add(gameFrame);
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Thread gameThread = new Thread(gameFrame);
        gameThread.start();
    }
}
