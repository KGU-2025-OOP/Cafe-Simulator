package ui;

public class MenuItem
{
    public enum MenuType
    {
        Beverage,
        Dessert
    }

    private String m_name;
    private MenuType m_type;
    private boolean m_isUnlocked;

    public MenuItem(String name, MenuType type, boolean isUnlocked)
    {
        m_name = name;
        m_type = type;
        m_isUnlocked = isUnlocked;
    }

    public String GetName()
    {
        return m_name;
    }

    public MenuType GetType()
    {
        return m_type;
    }

    public boolean IsUnlocked()
    {
        return m_isUnlocked;
    }
}