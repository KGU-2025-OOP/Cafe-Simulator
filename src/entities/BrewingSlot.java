package entities;

import util.Vector2f;
import java.awt.Graphics2D;
import java.awt.Font;
import java.util.ArrayList;

public class BrewingSlot implements IGameObject {

    private static final int LINE_COUNT = 5;

    private ArrayList<DropItem>[] lines;
    private float[] speeds;
    private int[] xPositions;
    private int[] failCount;

    private float depth = 0.f;
    private float startY = 0;
    private boolean gameOver = false;

    private DeadLine deadLine;
    private Font font;

    private static final String[] WORDS = { "아", "메", "리", "카", "노" };

    public BrewingSlot(int[] xPositions, float[] speeds, Font font, DeadLine deadLine) {
        this.xPositions = xPositions;
        this.speeds = speeds;
        this.font = font;
        this.deadLine = deadLine;

        lines = new ArrayList[LINE_COUNT];
        failCount = new int[LINE_COUNT];

        for (int i = 0; i < LINE_COUNT; i++) {
            lines[i] = new ArrayList<>();
            failCount[i] = 0;
        }
    }

    private String getRandomWord() {
        return WORDS[(int)(Math.random() * WORDS.length)];
    }

    public void addWordToAllLines() {
        for (int i = 0; i < LINE_COUNT; i++) {
            addWordToLine(i);
        }
    }

    public void addWordToLine(int lineIndex) {
        String word = getRandomWord();

        DropItem item = new DropItem(
                new Vector2f(xPositions[lineIndex], startY),
                new Vector2f(0, 1),
                speeds[lineIndex],
                word,
                font,
                deadLine
        );

        lines[lineIndex].add(item);
    }

    @Override
    public void update(float deltaTime) {

        if (gameOver) return;

        int totalFail = 0;

        for (int i = 0; i < LINE_COUNT; i++) {

            ArrayList<DropItem> list = lines[i];

            for (int j = list.size() - 1; j >= 0; j--) {
                DropItem item = list.get(j);
                item.update(deltaTime);

                if (!item.shouldRemove()) {
                    failCount[i]++;
                    list.remove(j);
                }
            }

            totalFail += failCount[i];
        }

        if (totalFail >= 2) {
            gameOver = true;
        }
    }

    public boolean matchInput(String input) {

        for (int i = 0; i < LINE_COUNT; i++) {

            ArrayList<DropItem> list = lines[i];

            for (int j = list.size() - 1; j >= 0; j--) {
                DropItem item = list.get(j);

                if (item.match(input)) {
                    list.remove(j);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void draw(Graphics2D g) {

        for (int i = 0; i < LINE_COUNT; i++) {

            for (DropItem item : lines[i]) {
                item.draw(g);
            }
        }
    }

    @Override
    public float getDepth() {
        return depth;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getLineFailCount(int lineIndex) {
        return failCount[lineIndex];
    }
}
