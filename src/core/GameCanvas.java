package core;

import entities.Background;
import entities.BrewingSlot;
import entities.DeadLine;
import order.OrderManager;
import util.KoreanInputAssembler;
import util.MessageQueue;
import graphics.RenderQueue;
import graphics.TextBox;
import graphics.FPSCounter;
import util.Vector2f;
import util.Vector2i;


import java.awt.Canvas;
import java.awt.Font;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import entities.WobbleImage;

public class GameCanvas extends Canvas implements Runnable {
    private static final long DAY_TIME = 15;
    private final RenderQueue renderQueue;
    private final MessageQueue messageQueue;
    private final KoreanInputAssembler korean;
    private final FPSCounter frameCounter;
    private int menuCounter;
    private int orderCounter;
    private long roundTimer;
    private long roundTime;

    public boolean shouldRun;
    private long lastTime;

    private final Background background;
    private ArrayList<BrewingSlot> brewingSlots;
    private ArrayList<Vector2i> brewingIDs;
    private ArrayList<WobbleImage> wobbleImages; // ★ 흔들리는 이미지 리스트 추가

    private OrderManager coffeeshopManager;
    private DeadLine deadLine;
    private TextBox inputBox;
    private final TextBox timerBox;
    private DayEndListener endEvent;
    private boolean paused;
    public boolean pause;

    public GameCanvas(int width, int height) {

        timerBox = new TextBox(new Vector2f(width - 45, height - 45), 0.F, new StringBuffer("" + roundTimer), new Font("Batang", Font.PLAIN, 25));
        renderQueue = new RenderQueue(width, height);
        messageQueue = new MessageQueue();
        frameCounter = new FPSCounter();
        korean = new KoreanInputAssembler();
        background = new Background(width, height);
        setSize(width, height);
        Toolkit.getDefaultToolkit().addAWTEventListener(messageQueue, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        paused = false;
        pause = false;
    }

    public void setDayEndListener(DayEndListener listener) {
        this.endEvent = listener;
    }

    public void run() {
        KeyEvent ki;
        MouseEvent mi;

        init();
        while (shouldRun) {
            if (pause) {
                paused = true;
                stop();
                continue;
            }
            if (paused) {
                paused = false;
                long remainTime = roundTimer - lastTime;
                lastTime = System.nanoTime();
                roundTimer = lastTime + remainTime;
            }
            // message handling
            AWTEvent input = messageQueue.poll();

            if (input != null) {
                StringBuffer text = inputBox.getBufferHandle();
                switch (input.getID()) {
                    case KeyEvent.KEY_TYPED:
                        ki = (KeyEvent) input;
                        korean.input(text, ki);
                        if (ki.isControlDown()) {
                            break;
                        }
                        char c = ki.getKeyChar();
                        if (c == 8) {
                            int length = text.length();
                            if (length > 0) {
                                text.setLength(length - 1);
                            }
                        } else if (c == '\n') {
                            for (BrewingSlot i : brewingSlots) {
                                if (i.matchInput(text.toString())) {
                                    break;
                                }
                            }
                            text.setLength(0);
                        } else {
                            if (!korean.isActivated()) {
                                text.append(c);
                            }

                        }

                        break;
                    case KeyEvent.KEY_PRESSED:
                        ki = (KeyEvent) input;
                        if (ki.getKeyCode() == KeyEvent.VK_ALT) {
                            korean.toggleActivation();
                        } else if (ki.getKeyCode() == KeyEvent.VK_UP) {
                            levelup();
                        } else if (ki.getKeyCode() == KeyEvent.VK_DOWN) {
                            levelDown();
                        }

                        break;
                    case KeyEvent.KEY_RELEASED:
                        ki = (KeyEvent) input;

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

    private void stop() {
        messageQueue.clear();
        renderQueue.clear();
        korean.toggleActivation();
        korean.toggleActivation();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() {
        paused = false;
        pause = false;
        // Initialize game objects
        int width = getWidth();
        int height = getHeight();
        messageQueue.clear();

        inputBox = new TextBox(
                new Vector2f(width / 2.F, height / 4.F),
                0.F,
                new StringBuffer(),
                new Font("Batang", Font.PLAIN, 30));
        deadLine = new DeadLine(width, height / (float) 4, new Vector2f(), 0.F);

        coffeeshopManager = new OrderManager();
        coffeeshopManager.createRandomOrder();
        brewingSlots = new ArrayList<BrewingSlot>();
        brewingIDs = new ArrayList<Vector2i>();
        brewingSlots.add(new BrewingSlot(width - 200, height, 100, deadLine));

        orderCounter = 0;
        menuCounter = 0;
        brewingIDs.add(new Vector2i(orderCounter, menuCounter));
        try {
            brewingSlots.get(0).loadMenu(coffeeshopManager.getOrder(orderCounter).getMenu(menuCounter));
            ++menuCounter;
            if (coffeeshopManager.getOrder(orderCounter).getMenuLength() <= menuCounter) {
                ++orderCounter;
                menuCounter = 0;
            }
            if (coffeeshopManager.getOrderSize() <= orderCounter) {
                coffeeshopManager.createRandomOrder();
            }
        } catch (IOException e) {
            System.out.println("Failed load image from loadMenu");
        }

        coffeeshopManager = new OrderManager();
        coffeeshopManager.createRandomOrder();
        brewingSlots = new ArrayList<BrewingSlot>();
        wobbleImages = new ArrayList<>();
        brewingSlots.add(new BrewingSlot(width, height, 0, deadLine));
        try {
            brewingSlots.get(0).loadMenu(coffeeshopManager.getOrder(0).getMenu(0));
        } catch (IOException e) {
            assert (true);
        }


        float bgObjDepth = 4.5F;

        wobbleImages.add(new WobbleImage(10, 70, "resources/image/menu_image/americano.png", bgObjDepth).setSize(100).setSpeed(4f));
        wobbleImages.add(new WobbleImage(30, 200, "resources/image/menu_image/earlgrey_tea.png", bgObjDepth).setSize(100).setSpeed(3.3f));
        wobbleImages.add(new WobbleImage(25, 330, "resources/image/menu_image/cold_brew.png", bgObjDepth).setSize(100).setSpeed(6f));

        wobbleImages.add(new WobbleImage(width - 90, 70, "resources/image/menu_image/einspanner.png", bgObjDepth).setSize(100).setSpeed(5f));
        wobbleImages.add(new WobbleImage(width - 100, 200, "resources/image/menu_image/apogatto.png", bgObjDepth).setSize(100).setSpeed(4f));
        wobbleImages.add(new WobbleImage(width - 85, 330, "resources/image/menu_image/lemonade.png", bgObjDepth).setSize(100).setSpeed(6.3f));
        // Start game loop;
        lastTime = System.nanoTime();
        roundTime = DAY_TIME * FPSCounter.secondInNanoTime;
        roundTimer = lastTime + roundTime;
        shouldRun = true;
    }

    private void update() {
        // Get delta time;
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastTime) / (float) FPSCounter.secondInNanoTime;
        lastTime = currentTime;

        if (roundTimer - lastTime > 0) {
            StringBuffer timer = timerBox.getBufferHandle();
            timer.setLength(0);
            timer.append((roundTimer - lastTime) / FPSCounter.secondInNanoTime);
        } else {
            // TODO: File IO append
            try {
                FileWriter salesFW = new FileWriter("sales.txt", true);
                salesFW.append(coffeeshopManager.toString());
                salesFW.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int revenue = 0;
            for (int i = 0; i < orderCounter - 1; ++i) {
                revenue += coffeeshopManager.getOrder(i).getPrice();
            }
            try {
                FileWriter revenueFW = new FileWriter("daily_revenue.txt", true);
                String content = coffeeshopManager.getDay() + " " + revenue + "\n";
                revenueFW.append(content);
                revenueFW.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            coffeeshopManager.nextDay();
            inputBox.getBufferHandle().setLength(0);
            lastTime = System.nanoTime();
            roundTime = DAY_TIME * FPSCounter.secondInNanoTime;
            roundTimer = lastTime + roundTime;
            coffeeshopManager.createRandomOrder();
            int orderCounterBak = orderCounter;
            orderCounter = 0;
            menuCounter = 0;
            for (int i = 0; i < brewingSlots.size(); ++i) {
                try {
                    brewingIDs.get(i).x = orderCounter;
                    brewingIDs.get(i).y = menuCounter;
                    brewingSlots.get(i).loadMenu(coffeeshopManager.getOrder(orderCounter).getMenu(menuCounter));
                    ++menuCounter;
                    if (coffeeshopManager.getOrder(orderCounter).getMenuLength() <= menuCounter) {
                        ++orderCounter;
                        menuCounter = 0;
                    }
                    if (coffeeshopManager.getOrderSize() <= orderCounter) {
                        coffeeshopManager.createRandomOrder();
                    }
                } catch (IOException e) {
                    assert (true);
                }
            }
            endEvent.onDayEnd(orderCounterBak - 1, revenue);
        }
        timerBox.update(deltaTime);
        // update
        inputBox.update(deltaTime);
        // FailLine.line.update(deltaTime);
        for (int i = 0; i < brewingSlots.size(); ++i) {
            brewingSlots.get(i).update(deltaTime);
            if (brewingSlots.get(i).isEmpty()) {
                if (coffeeshopManager.getOrder(brewingIDs.get(i).x).serve(brewingIDs.get(i).y)) {
                    // order 서빙완료
                    // Placeholder
                }
                try {
                    brewingSlots.get(i).loadMenu(coffeeshopManager.getOrder(orderCounter).getMenu(menuCounter));
                    brewingIDs.get(i).x = orderCounter;
                    brewingIDs.get(i).y = menuCounter;
                    ++menuCounter;
                    if (coffeeshopManager.getOrder(orderCounter).getMenuLength() <= menuCounter) {
                        ++orderCounter;
                        menuCounter = 0;
                    }
                    if (coffeeshopManager.getOrderSize() <= orderCounter) {
                        coffeeshopManager.createRandomOrder();
                    }
                } catch (IOException e) {
                    assert (true);
                }
            }
        }
        for (WobbleImage w : wobbleImages)
            w.update(deltaTime);
    }

    private void render() {
        // Add rendering object
        renderQueue.add(inputBox);
        renderQueue.add(timerBox);
        renderQueue.add(deadLine);
        renderQueue.add(background);
        for (BrewingSlot i : brewingSlots) {
            renderQueue.add(i);
        }
        // Draw
        drawCanvas();
        for (WobbleImage w : wobbleImages)
            renderQueue.add(w);

    }

    private void drawCanvas() {
        BufferedImage buffer = renderQueue.flush();
        Graphics g = getGraphics();
        assert (g != null);
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
    }

    private void levelup() {
        if (brewingSlots.size() < 4) {
            int newSlotCount = brewingSlots.size() + 1;
            int newWidth = (getWidth() - 200) / newSlotCount;
            int height = getHeight();
            BrewingSlot newBrewingSlot = new BrewingSlot(newWidth, height, newWidth * (newSlotCount - 1) + 100, deadLine);
            brewingSlots.add(newBrewingSlot);
            brewingIDs.add(new Vector2i(orderCounter, menuCounter));
            try {
                newBrewingSlot.loadMenu(coffeeshopManager.getOrder(orderCounter).getMenu(menuCounter));
                ++menuCounter;
                if (coffeeshopManager.getOrder(orderCounter).getMenuLength() <= menuCounter) {
                    ++orderCounter;
                    menuCounter = 0;
                }
                if (coffeeshopManager.getOrderSize() <= orderCounter) {
                    coffeeshopManager.createRandomOrder();
                }
            } catch (IOException e) {
                assert (true);
            }
            for (int i = 0; i < newSlotCount - 1; ++i) {
                brewingSlots.get(i).resize(newWidth, height, newWidth * i + 100);
            }

        }

        coffeeshopManager.upgrade();
    }

    private void levelDown() {
        if (brewingSlots.size() > 1) {
            int newSlotCount = brewingSlots.size() - 1;
            int newWidth = getWidth() / newSlotCount;
            int height = getHeight();
            brewingSlots.get(newSlotCount).clear();
            brewingSlots.remove(newSlotCount);
            brewingIDs.remove(newSlotCount);
            for (int i = 0; i < newSlotCount; ++i) {
                brewingSlots.get(i).resize(newWidth, height, i * newWidth);
            }
        }
        coffeeshopManager.downgrade();
    }

    private void shutdown() {
        System.exit(0);
    }

    private MessageQueue getMessageQueue() {
        return messageQueue;
    }

}
