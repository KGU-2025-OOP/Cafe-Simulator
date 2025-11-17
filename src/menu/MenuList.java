package menu;

import java.util.Scanner;
import java.util.ArrayList;

public class MenuList {
    ArrayList<Menu> menuList;

    public MenuList(ArrayList<Ingredient> ingredientList) {
        this.menuList = new ArrayList<Menu>();
        try {
            java.io.File file = new java.io.File("resources/menu.txt");
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                Menu menu = new Menu();
                menu.readMenu(filein, ingredientList);
                this.menuList.add(menu);
            }
            filein.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }
    }

    public ArrayList<Menu> getMenuList() {
        return this.menuList;
    }
}
