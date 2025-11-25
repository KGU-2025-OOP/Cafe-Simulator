package order;

import menu.Menu;

import java.util.ArrayList;

public class Order {
    private ArrayList<Menu> items;
    private ArrayList<Boolean> served;
    private static int dailyOrderCount = 0;
    private int orderID;
    private int price;
    private long timestamp;

    public Order(Menu firstMenu) {
        items = new ArrayList<Menu>();
        items.add(firstMenu);
        served = new ArrayList<Boolean>();
        served.add(false);
        price += firstMenu.getPrice();
        orderID = dailyOrderCount;
        ++dailyOrderCount;
        timestamp = System.currentTimeMillis();
    }

    public void addMenu(Menu item) {
        items.add(item);
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
    public static void nextDay() {
        dailyOrderCount = 0;
    }
}