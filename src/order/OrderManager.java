package order;

import entities.DeadLine;
import entities.DropItem;
import menu.*;
import util.Vector2f;

import java.awt.Font;

import java.util.ArrayList;
import java.util.Random;

public class OrderManager {
    private int day;
    private ArrayList<Order> orders;
    private IngredientList ingredientList;
    private MenuList menuList;
    private OptionList optionList;
    private Random rand;
    private int availableMenuCount;
    private int maxMenuCount;
    private int maxOptionCount;

    public OrderManager(int day) {
        orders = new ArrayList<Order>();
        optionList = new OptionList();
        ingredientList = new IngredientList();
        menuList = new MenuList(ingredientList.getIngredientList());
        rand = new Random();
        this.day = day;
        availableMenuCount = 4;
        availableMenuCount = menuList.size();
        maxMenuCount = 3;
        maxOptionCount = 2;
    }

    public void createRandomOrder() {
        Order newItem = new Order(menuList.get(rand.nextInt(availableMenuCount)));
        int newMenuLength = rand.nextInt(maxMenuCount);
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
}