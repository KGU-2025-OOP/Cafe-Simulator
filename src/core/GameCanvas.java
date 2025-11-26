package core;

import entities.BrewingSlot;
import entities.DeadLine;
import order.OrderManager;
import util.KoreanInputAssembler;
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
import java.util.ArrayList;

public class GameCanvas extends Canvas implements Runnable {

    private RenderQueue renderQueue;
    private MessageQueue messageQueue;
    private KoreanInputAssembler korean;
    FPSCounter frameCounter;

    public boolean shouldRun;
    private long lastTime;

    private ArrayList<BrewingSlot> brewingSlots;
    private OrderManager coffeeshopManager;
    private static int day;

    private class InputBox {
        public static TextBox box;
        // Handle
        public static StringBuffer text;

        public static void init(int width, int height) {
            box = new TextBox(
                    new Vector2f(width / 2.F, height / 4.F),
                    0.F,
                    new StringBuffer(),
                    new Font("Batang", Font.PLAIN, 30));
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
        korean = new KoreanInputAssembler();
        setSize(width, height);
        Toolkit.getDefaultToolkit().addAWTEventListener(messageQueue, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    private void init() {
        // Initialize game objects
        int width = getWidth();
        int height = getHeight();

        InputBox.init(width, height);
        FailLine.init(width, height);
        coffeeshopManager = new OrderManager(day);
        coffeeshopManager.createRandomOrder();
        brewingSlots = new ArrayList<BrewingSlot>();
        brewingSlots.add(new BrewingSlot(width, height, 0));
        brewingSlots.get(0).loadMenu(
                coffeeshopManager.getOrder(0).getMenu(0).getName(),
                coffeeshopManager.getOrder(0).getMenu(0).getDrops(0, FailLine.line));


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
        for (BrewingSlot i : brewingSlots) {
            i.update(deltaTime);
        }
    }

    private void render() {
        // Add rendering object
        renderQueue.add(InputBox.box);
        renderQueue.add(FailLine.line);
        for (BrewingSlot i : brewingSlots) {
            renderQueue.add(i);
        }
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
                        korean.input(InputBox.text, ki);
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
                            if (!korean.isActivated()) {
                                InputBox.text.append(c);
                            }

                        }

                        break;
                    case KeyEvent.KEY_PRESSED:
                        ki = (KeyEvent)input;
                        if (ki.getKeyCode() == KeyEvent.VK_ALT) {
                            korean.toggleActivation();
                        } else if (ki.getKeyCode() == KeyEvent.VK_UP) {
                            int newSlotCount = brewingSlots.size() + 1;
                            int newWidth = getWidth() / newSlotCount;
                            int height = getHeight();
                            brewingSlots.add(new BrewingSlot(newWidth, height, newWidth * (newSlotCount - 1)));

                            for (int i = 0; i < newSlotCount - 1; ++i) {
                                brewingSlots.get(i).resize(newWidth, height, i * newWidth);
                            }


                        } else if (ki.getKeyCode() == KeyEvent.VK_DOWN) {
                            int newSlotCount = brewingSlots.size() - 1;
                            if (newSlotCount > 0) {
                                int newWidth = getWidth() / newSlotCount;
                                int height = getHeight();
                                brewingSlots.remove(newSlotCount);
                                for (int i = 0; i < newSlotCount; ++i) {
                                    brewingSlots.get(i).resize(newWidth, height, i * newWidth);
                                }
                            }
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
        // shutdown();
        nextLevel();
    }

    private void drawCanvas() {
        BufferedImage buffer = renderQueue.flush();
        Graphics g = getGraphics();
        assert (g != null);
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
    }

    private void nextLevel() {

    }
    private void shutdown() {
        System.exit(0);
    }

    private MessageQueue getMessageQueue() {
        return messageQueue;
    }

}
