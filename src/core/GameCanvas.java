package core;

import entities.BrewingSlot;
import entities.DeadLine;
import util.MessageQueue;
import graphics.RenderQueue;
import graphics.TextBox;
import graphics.FPSCounter;
import util.Vector2f;
import entities.DropItem;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.beancontext.BeanContext;
import java.io.IOException;
import java.util.ArrayList;

public class GameCanvas extends Canvas implements Runnable {

    private RenderQueue renderQueue;
    private MessageQueue messageQueue;
    FPSCounter frameCounter;

    public boolean shouldRun;
    private long lastTime;

    private ArrayList<DropItem> drops;
    private ArrayList<BrewingSlot> brewingSlots;

    private class InputBox {
        public static TextBox box;

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

        int width = getWidth();
        int height = getHeight();

        InputBox.init(width, height);
        FailLine.init(width, height);
        Font dropsFont = new Font("Arial", Font.BOLD, 12);
        drops = new ArrayList<DropItem>();
        for (int i = 0; i < 10; ++i) {
            drops.add(new DropItem(
                    new Vector2f(width / 10 * i, height),
                    new Vector2f(0, -1.F), 25.F, "temp str " + i, dropsFont, FailLine.line));
        }
        brewingSlots = new ArrayList<BrewingSlot>();
        brewingSlots.add(new BrewingSlot(drops, width, height, 0));



        lastTime = System.nanoTime();
        shouldRun = true;
    }

    private void update() {

        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastTime) / (float) FPSCounter.secondInNanoTime;
        lastTime = currentTime;


        InputBox.box.update(deltaTime);
        FailLine.line.update(deltaTime);
        for (BrewingSlot i : brewingSlots) {
            i.update(deltaTime);
        }
    }

    private void render() {

        renderQueue.add(InputBox.box);
        renderQueue.add(FailLine.line);
        for (BrewingSlot i : brewingSlots) {
            renderQueue.add(i);
        }

        drawCanvas();
    }

    public void run() {
        init();

        KeyEvent ki;
        MouseEvent mi;

        while (shouldRun) {

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
                            for (BrewingSlot i : brewingSlots) {
                                if (i.matchInput(InputBox.text.toString())) {
                                    break;
                                }
                            }
                            InputBox.text.setLength(0);
                        } else {
                            InputBox.text.append(c);
                        }

                        break;
                    case KeyEvent.KEY_PRESSED:
                        ki = (KeyEvent)input;
                        if (ki.getKeyCode() == KeyEvent.VK_UP) {
                            int newSlotCount = brewingSlots.size() + 1;
                            int newWidth = getWidth() / newSlotCount;
                            int height = getHeight();
                            for (int i = 0; i < newSlotCount; ++i) {
                                brewingSlots.add(new BrewingSlot(drops, newWidth, height, newWidth * (newSlotCount - 1)));
                            }
                            for (int i = 0; i < newSlotCount - 1; ++i) {
                                brewingSlots.get(i).resize(newWidth, height, i * newWidth);
                            }


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
        //System.exit(0);
        System.out.println("Game Canvas Thread Stopped.");
    }

}