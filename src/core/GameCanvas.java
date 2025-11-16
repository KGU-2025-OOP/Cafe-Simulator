package core;

import entities.DeadLine;
import entities.DropItem;
import entities.IGameObject;
import util.MessageQueue;
import graphics.RenderQueue;
import graphics.TextBox;
import graphics.FPSCounter;
import util.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameCanvas extends Canvas implements Runnable {

    private RenderQueue renderQueue;
    private MessageQueue messageQueue;
    FPSCounter frameCounter;

    public boolean shouldRun;
    private long lastTime;

    private class InputBox {
        public static TextBox box;
        // Handle
        public static StringBuffer text;

        public static void init(int width, int height) {
            box = new TextBox(
                    new Vector2f(width / 2.F, height / 4.F),
                    0.F,
                    new StringBuffer(),
                    new Font("Arial", Font.ITALIC, 30));
            text = box.getBufferHandle();
        }
    }

    private class FailLine {
        public static DeadLine line;
        // Handle
        public static Vector2f move;
        public static void init(int width, int height) {
            line = new DeadLine(width, height / 3, new Vector2f(), 15.F);
            move = line.getMoveHandle();
        }
    }

    public GameCanvas(int width, int height) {
        renderQueue = new RenderQueue(width, height);
        messageQueue = new MessageQueue();
        frameCounter = new FPSCounter();
        setSize(width, height);
        Toolkit.getDefaultToolkit().addAWTEventListener(messageQueue, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    private void init() {
        // Initialize game objects
        int width = getWidth();
        int height = getHeight();

        InputBox.init(width, height);
        FailLine.init(width, height);

        // Start game loop;
        lastTime = System.nanoTime();
        shouldRun = true;
    }

    private void update() {
        // Get delta time;
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastTime) / (float) FPSCounter.secondInNanoTime;
        lastTime = currentTime;

        // update
        InputBox.box.update(deltaTime);
        FailLine.line.update(deltaTime);
    }

    private void render() {
        // Add rendering object
        renderQueue.add(InputBox.box);
        renderQueue.add(FailLine.line);
        // Draw
        drawCanvas();
    }

    public void run() {
        init();

        KeyEvent ki;
        MouseEvent mi;

        while (shouldRun) {
            // message handling
            AWTEvent input = messageQueue.poll();

            if (input != null) {
                switch (input.getID()) {
                    case KeyEvent.KEY_TYPED:
                        ki = (KeyEvent)input;
                        if (ki.isControlDown()) {
                            break;
                        }
                        char c = ki.getKeyChar();
                        if (c == 8) {
                            int length = InputBox.text.length();
                            if (length > 0) {
                                InputBox.text.setLength(length - 1);
                            }
                        } else if (c == '\n') {
                            InputBox.text.setLength(0);
                        } else {
                            InputBox.text.append(c);
                        }

                        break;
                    case KeyEvent.KEY_PRESSED:
                        ki = (KeyEvent)input;
                        if (ki.getKeyCode() == KeyEvent.VK_UP) {

                        } else if (ki.getKeyCode() == KeyEvent.VK_DOWN) {

                        }

                        break;
                    case KeyEvent.KEY_RELEASED:
                        ki = (KeyEvent)input;

                        break;
                    case MouseEvent.MOUSE_CLICKED:
                        mi = (MouseEvent) input;
                        break;
                    default:
                        break;
                }
            }
            update();
            render();
            // frameCounter.limitFPS(60);
            frameCounter.printFPS();
        }
        shutdown();
    }

    private void drawCanvas() {
        BufferedImage buffer = renderQueue.flush();
        Graphics g = getGraphics();
        assert (g != null);
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
    }

    private void shutdown() {
        System.exit(0);
    }

}
