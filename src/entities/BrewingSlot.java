package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import graphics.TextBox;
import menu.Menu;
import util.ImageUtils;
import util.Vector2f;
import util.Vector2i;

import javax.imageio.ImageIO;

public class BrewingSlot implements IGameObject {

    private static final int SLOT_COUNT = 3;
    private final DropItem[] slots; // Items currently displayed on screen
    private final Vector2f[] slotPos;
    private ArrayList<DropItem> queue; // Items waiting to be processed
    private int slotWidth; // Current slot width
    private final int xPos;
    private TextBox currentMenu;
    private final Font menuFont;
    private final int height;
    private final int topMargin;
    private final DeadLine deadline;


    public BrewingSlot(int slotWidth, int height, int xPos, DeadLine deadline) {
        topMargin = 75;
        menuFont = new Font("Batang", Font.BOLD, 24);
        this.slotWidth = slotWidth;
        this.height = height;
        this.slots = new DropItem[SLOT_COUNT];
        slotPos = new Vector2f[SLOT_COUNT];
        for (int i = 0; i < SLOT_COUNT; ++i) {
            slotPos[i] = new Vector2f(slotWidth / (float)SLOT_COUNT * i + slotWidth / (2 * (float)SLOT_COUNT) + xPos, height - topMargin);
        }
        this.xPos = xPos;
        this.deadline = deadline;
    }

    public void loadMenu(Menu menu) throws IOException {

        final float imageSize = this.slotWidth * 0.2F;
        queue = menu.getDrops(deadline);
        BufferedImage menuImage = ImageIO.read(menu.getImage());
        float ratio = menuImage.getHeight() / (float) menuImage.getWidth();

        currentMenu = new DropItem(new Vector2f(xPos + slotWidth / (float)2, height - topMargin), new Vector2f(), 0.F,
                menu.getName(), menuFont, ImageUtils.resize(menuImage, (int) imageSize, (int) (imageSize * ratio)), null);
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
        fillEmptySlots(false);
    }

    @Override
    public void draw(Graphics2D g) {
        // draw menu name
        if (currentMenu == null) {
            return;
        }
        currentMenu.draw(g);
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

    public boolean isEmpty() {
        boolean slotAlsoEmpty = true;
        for (var i : slots) {
            if (i != null) {
                slotAlsoEmpty = false;
                break;
            }
        }
        return queue.isEmpty() && slotAlsoEmpty;
    }

    public void clear() {
        currentMenu = null;
        queue.clear();
        for (int i = 0; i < slots.length; ++i) {
            slots[i] = null;
        }
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
        Vector2f cmenuPos = currentMenu.getPositionHandle();

        cmenuPos.x = (cmenuPos.x - this.xPos) * scale + xPos;

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