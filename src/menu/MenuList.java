package menu;

import java.util.Scanner;
import java.util.ArrayList;

public class MenuList {
    private ArrayList<Menu> menuList;
    private static final String MENU_PATH = "resources/menu.txt";

    public MenuList(ArrayList<Ingredient> ingredientList) {
        this.menuList = new ArrayList<Menu>();
        loadFromFile(ingredientList);
    }

    private void loadFromFile(ArrayList<Ingredient> ingredientList) {
        try {
            java.io.File file = new java.io.File(MENU_PATH);
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                Menu menu = new Menu();
                menu.readMenu(filein, ingredientList);
                this.menuList.add(menu);
            }
            filein.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
    }

    public ArrayList<Menu> getMenuList() {
        return this.menuList;
    }

    public Menu findByName(String name) {
        for (Menu menu : menuList) {
            if (menu.getName().equals(name)) {
                return menu;
            }
        }
        return null;
    }

    public int size() {
        return menuList.size();
    }

    public Menu get(int index) {
        return menuList.get(index);
    }
}
