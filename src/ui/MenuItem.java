package ui;

public class MenuItem
{
    public enum MenuType { BEVERAGE, DESSERT }

    private String name;
    private MenuType type;
    private boolean isUnlocked;

    public MenuItem(String name, MenuType type, boolean isUnlocked)
    {
        this.name = name;
        this.type = type;
        this.isUnlocked = isUnlocked;
    }

    public String getName() { return this.name; }
    public MenuType getType() { return this.type; }
    public boolean isUnlocked() { return this.isUnlocked; }
}