package menu;

import java.io.File;

import java.util.Scanner;
import java.util.ArrayList;

public class Test {
    ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
    ArrayList<Menu> menuList = new ArrayList<Menu>();
    public static void main(String[] args) {
        Test test = new Test();
        test.myMain();
    }

    void myMain() {
        readMenuTest();
        printMenuTest();
    }

    void readMenuTest() {
        try {
            File file = new File("src/menu/ingredients.txt");
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                ingredientList.add(new Ingredient(filein));
            }
            filein.close();

            file = new File("src/menu/menu.txt");
            filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                Menu menu = new Menu();
                menu.readMenu(filein, ingredientList);
                menuList.add(menu);
            }
            filein.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }
        System.out.println("Menu read test passed.");
    }

    void printMenuTest() {
        for (Menu menu : menuList) {
            menu.printMenu();
        }
        System.out.println("Menu print test completed.");
    }
}
