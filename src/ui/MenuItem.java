package ui;

import java.util.List;
import java.util.ArrayList;

public class MenuItem {
    public enum MenuType {
        Coffee,
        NonCoffee,
        Dessert    // 베이커리/디저트 (기존 유지)
    }

    private String name;
    private MenuType type;
    private boolean unlocked;

    private int price;
    private List<String> ingredients;
    private String imagePath;

    public MenuItem(String name, MenuType type, boolean unlocked) {
        this(name, type, unlocked, 0, new ArrayList<>(), "");
    }

    public MenuItem(String name, MenuType type, boolean unlocked, int price, List<String> ingredients, String imagePath) {
        this.name = name;
        this.type = type;
        this.unlocked = unlocked;
        this.price = price;
        this.ingredients = ingredients;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public MenuType getType() { return type; }
    public boolean isUnlocked() { return unlocked; }

    public int getPrice() { return price; }
    public List<String> getIngredients() { return ingredients; }
    public String getImagePath() { return imagePath; }
}