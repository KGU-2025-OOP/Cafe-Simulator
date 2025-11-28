package order;

import menu.*;

import java.util.ArrayList;
import java.util.Random;

public class OrderManager {
    private int day;
    private final ArrayList<Order> orders;
    private IngredientList ingredientList;
    private MenuList menuList;
    private OptionList optionList;
    private Random rand;
    private int availableMenuCount;
    private int maxMenuCount;
    private int maxOptionCount;

    public OrderManager() {
        orders = new ArrayList<Order>();
        optionList = new OptionList();
        ingredientList = new IngredientList();
        menuList = new MenuList(ingredientList.getIngredientList());
        rand = new Random();
        day = 1;
        // availableMenuCount = 4;
        availableMenuCount = menuList.size();
        maxMenuCount = 2;
        maxOptionCount = 1;
    }

    public void createRandomOrder() {
        Order newItem = new Order(menuList.get(rand.nextInt(availableMenuCount)));
        int newMenuLength = rand.nextInt(1, maxMenuCount);
        for (int i = 0; i < newMenuLength; ++i) {
            newItem.addMenu(menuList.get(rand.nextInt(availableMenuCount)));
        }
        int length = newItem.getMenuLength();
        for (int i = 0; i < length; ++i) {
            int optionCount = rand.nextInt(maxOptionCount);
            for (int j = 0; j < optionCount; ++j) {
                newItem.getMenu(i).addOptions(optionList.get(rand.nextInt(optionList.size())));
            }
        }

        orders.add(newItem);
    }

    public Order getOrder(int idx) {
        return orders.get(idx);
    }

    public int getOrderSize() {
        return orders.size();
    }

    public void nextDay() {
        orders.clear();
        ++day;
        // day save routine
    }

    public void upgrade() {
        if (maxMenuCount < 6) {
            maxMenuCount++;
        }
        maxOptionCount++;
    }

    public void downgrade() {
        if (maxMenuCount >  1) {
            maxMenuCount--;
        }
        if (maxOptionCount > 0) {
            maxOptionCount--;
        }
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        for (var i : orders) {
            if (!i.isServed()) {
                break;
            }
            int menuLength = i.getMenuLength();
            for (int j = 0; j < menuLength; ++j) {
                strBuf.append(day);
                strBuf.append(" ");
                strBuf.append(i.toString());
                strBuf.append(" ");
                strBuf.append(i.getMenu(j).toString());
                strBuf.append('\n');
            }
        }
        return strBuf.toString();
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}