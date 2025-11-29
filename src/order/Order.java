package order;

import entities.DeadLine;
import entities.DropItem;
import menu.Ingredient;
import menu.Menu;
import util.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public class Order {
    private ArrayList<Menu> items;
    private ArrayList<Boolean> served;
    private static int dailyOrderCount = 0;
    private int orderID;
    private int price;
    private long timestamp;


    private float dropSpeed;

    public Order(Menu firstMenu) {
        items = new ArrayList<Menu>();
        items.add(firstMenu);
        served = new ArrayList<Boolean>();
        served.add(false);
        price += firstMenu.getPrice();
        orderID = dailyOrderCount;
        ++dailyOrderCount;
        timestamp = System.currentTimeMillis();
        dropSpeed = 15.F;
    }

    public static void nextDay() {
        dailyOrderCount = 0;
    }

    public void addMenu(Menu item) {
        items.add(item);
        served.add(false);
        price += item.getPrice();
    }

    public int getMenuLength() {
        return items.size();
    }

    public Menu getMenu(int idx) {
        return items.get(idx);
    }

    public boolean serve(int idx) {
        served.set(idx, true);

        for (var i : served) {
            if (i == false) {
                return false;
            }
        }
        return true;
    }

    public boolean isServed() {
        for (var i : served) {
            if (i == false) {
                return false;
            }
        }
        return true;
    }

    public int getPrice() {
        return price;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(timestamp);
        strBuf.append(" ");
        strBuf.append(orderID);
        return strBuf.toString();
    }
}