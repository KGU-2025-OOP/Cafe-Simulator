package game;

import graphics.RenderQueue;
import test.TestObject;
import graphics.FPSCounter;
import util.Vector2f;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import java.util.ArrayList;

public class GameCanvas extends Canvas implements Runnable {

    private RenderQueue renderQueue;
    private MessageQueue messageQueue;
    FPSCounter frameCounter;

    public boolean shouldRun;
    private long lastTime;

    private static class Player {
        public static TestObject tobj;
        public static Vector2f displacement;
        public static float speed;
        public static Vector2f direction;
        public static Vector2f move;
    }

    public GameCanvas(int width, int height) {
        renderQueue = new RenderQueue(width, height);
        messageQueue = new MessageQueue();
        frameCounter = new FPSCounter();
        setSize(width, height);
        Toolkit.getDefaultToolkit().addAWTEventListener(messageQueue, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    public void run() {
        init();
        KeyEvent ke;
        MouseEvent me;
        while (shouldRun) {
            // message handling
            AWTEvent input = messageQueue.poll();

            if (input != null) {
                switch (input.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        ke = (KeyEvent) input;
                        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                            System.out.println("Space key down");
                            renderQueue.setScreenResetEnabled(false);
                        } else if (ke.getKeyCode() == KeyEvent.VK_W) {
                            Player.direction.y = -1.0F;
                        } else if (ke.getKeyCode() == KeyEvent.VK_D) {
                            Player.direction.x = 1.F;
                        } else if (ke.getKeyCode() == KeyEvent.VK_S) {
                            Player.direction.y = 1.0F;
                        } else if (ke.getKeyCode() == KeyEvent.VK_A) {
                            Player.direction.x = -1.F;
                        }
                        break;
                    case KeyEvent.KEY_RELEASED:
                        ke = (KeyEvent) input;
                        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                            System.out.println("Space key up");
                            renderQueue.setScreenResetEnabled(true);
                        } else if (ke.getKeyCode() == KeyEvent.VK_W) {
                            Player.direction.y = 0.F;
                        } else if (ke.getKeyCode() == KeyEvent.VK_D) {
                            Player.direction.x = 0.F;
                        } else if (ke.getKeyCode() == KeyEvent.VK_S) {
                            Player.direction.y = 0.0F;
                        } else if (ke.getKeyCode() == KeyEvent.VK_A) {
                            Player.direction.x = 0.F;
                        }
                        break;
                    case MouseEvent.MOUSE_CLICKED:
                        me = (MouseEvent) input;
                        System.out.println(me.getPoint().toString());

                        break;
                    default:
                        break;
                }
            }
            update();
            render();
            frameCounter.limitFPS(60);
            frameCounter.printFPS();
        }
        shutdown();
    }

    private void init() {
        // Initialize Gmae objects
        Player.displacement = new Vector2f(0.F, 0.F);
        Player.speed = 150.0F;
        Player.tobj = new TestObject(getWidth(), getHeight(), 0.5F, Player.displacement);
        Player.direction = new Vector2f(0.F, 0.F);
        Player.move = new Vector2f();

        lastTime = System.nanoTime();

        shouldRun = true;
    }

    private void update() {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastTime) / (float) FPSCounter.secondInNanoTime;
        lastTime = currentTime;
        Player.move.x = Player.direction.x;
        Player.move.y = Player.direction.y;
        if (Player.move.getLength() > 0) {
            Player.move.normalize();
        }
        Player.move.scale(Player.speed);
        Player.displacement.x = Player.move.x;
        Player.displacement.y = Player.move.y;
        Player.tobj.update(deltaTime);

    }

    private void render() {
        renderQueue.add(Player.tobj);
        drawCanvas();
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
