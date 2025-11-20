package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;

import util.Vector2f;

public class BrewingSlot implements IGameObject {

    private static final int SLOT_COUNT = 3;
    private DropItem[] slots; // Items currently displayed on screen
    private Vector2f[] slotPos;
    private ArrayList<DropItem> queue; // Items waiting to be processed
    private int slotWidth; // Current slot width
    private int xPos;

    public BrewingSlot(ArrayList<DropItem> queue, int slotWidth, int height, int xPos) {
        this.queue = new ArrayList<DropItem>(queue);
        this.slotWidth = slotWidth;
        this.slots = new DropItem[SLOT_COUNT];
        slotPos = new Vector2f[SLOT_COUNT];
        for (int i = 0; i < SLOT_COUNT; ++i) {
            slotPos[i] = new Vector2f(slotWidth / SLOT_COUNT * i + slotWidth / (2 * SLOT_COUNT) + xPos, height);
        }
        this.xPos = xPos;
        fillEmptySlots(false);
    }

    /*
     * Fill empty slots with items from the queue
     */
    private void fillEmptySlots(boolean randomize) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] == null && !queue.isEmpty()) {
                // Use last element to avoid shifting cost (LIFO for performance)
                slots[i] = queue.remove(queue.size() - 1);
                Vector2f pos = slots[i].getPositionHandle();

                int idx = i;
                if (randomize) {
                    idx = (int) (Math.random() * SLOT_COUNT);
                }
                pos.x = slotPos[idx].x + ((float) Math.random() - 0.5F) * slotWidth / 10;
                pos.y = slotPos[idx].y;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            DropItem item = slots[i];

            // Skip if slot is empty
            if (item == null) {
                continue;
            }
            // Update current item
            item.update(deltaTime);

            // Remove expired item and refill slot
            if (!item.shouldRemove()) {
                slots[i] = null;
            }
        }
        // Ensure all empty slots are filled
        fillEmptySlots(true);
    }

    @Override
    public void draw(Graphics2D g) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] != null) {
                slots[i].draw(g);
            }
        }
    }

    /*
     * Match input with items in slots and remove if matched
     */
    public boolean matchInput(String input) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            DropItem item = slots[i];
            if (item != null && item.match(input)) {
                slots[i] = null;
                return true;
            }
        }
        return false;
    }

    /*
     * Resize slot width and reposition items proportionally
     */
    public void resize(int newWidth, int height, int xPos) {
        if (newWidth <= 0) {
            return;
        }

        float scale = newWidth / (float) slotWidth; // Calculate scaling ratio
        slotWidth = newWidth;


        // Scale positions of items in slots
        for (int i = 0; i < SLOT_COUNT; i++) {
            slotPos[i].x = (slotPos[i].x - this.xPos) * scale + xPos;
            slotPos[i].y = height;

            if (slots[i] != null) {
                slots[i].getPositionHandle().x = slotPos[i].x;
            }
        }
    }

    @Override
    public float getDepth() {
        return 1.F;
    }
}