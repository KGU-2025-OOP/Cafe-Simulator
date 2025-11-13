package game;

import graphics.RenderQueue;
import test.TestObject;
import graphics.FPSCounter;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import java.util.ArrayList;

public class GameFrame extends Canvas implements Runnable {

    private RenderQueue renderQueue;
    private MessageQueue messageQueue;
    FPSCounter frameCounter;

    private boolean shouldRun;


    private static class GameObjects {
        public static ArrayList<IGameObject> list;
    }

    public GameFrame(int width, int height) {
        renderQueue = new RenderQueue(width, height);
        messageQueue = new MessageQueue();
        frameCounter = new FPSCounter();
        setSize(width, height);
        Toolkit.getDefaultToolkit().addAWTEventListener(messageQueue, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);

    }

    public void run() {
        init();

        while (shouldRun) {
            // message handling
            AWTEvent input = messageQueue.poll();

            if (input != null) {
                switch (input.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (((KeyEvent) input).getKeyCode() == KeyEvent.VK_SPACE) {
                            System.out.println("Space key down");
                            renderQueue.setScreenResetEnabled(false);
                        }
                        break;
                    case KeyEvent.KEY_RELEASED:
                        if (((KeyEvent) input).getKeyCode() == KeyEvent.VK_SPACE) {
                            System.out.println("Space key up");
                            renderQueue.setScreenResetEnabled(true);
                        }
                        break;
                    case MouseEvent.MOUSE_CLICKED:
                        System.out.println(((MouseEvent) input).getPoint().toString());

                        break;
                    default:
                        break;
                }
            }
            update();
            render();
            frameCounter.printFPS();
        }
    }

    private void init() {
        GameObjects.list = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            GameObjects.list.add(new TestObject(getWidth(), getHeight() / (i + 1)));
        }


        shouldRun = true;

    }

    private void update() {
        for (IGameObject i : GameObjects.list) {
            i.update();
        }
    }

    private void render() {
        for (IGameObject i : GameObjects.list) {
            renderQueue.add(i);
        }

        drawCanvas();
    }

    private void drawCanvas() {
        BufferedImage buffer = renderQueue.flush();
        Graphics g = getGraphics();
        assert (g != null);
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
    }


}
