package ui;

public class MenuItem {
    public enum MenuType {
        Beverage,
        Dessert
    }

    private String name;
    private MenuType type;
    private boolean unlocked;

    public MenuItem(String name, MenuType type, boolean unlocked) {
        this.name = name;
        this.type = type;
        this.unlocked = unlocked;
    }

    public String getName() {
        return name;
    }

    public MenuType getType() {
        return type;
    }

    public boolean isUnlocked() {
        return unlocked;
    }
}
