package test;

import menu.Ingredient;
import menu.Menu;

import java.io.File;

import java.util.Scanner;
import java.util.ArrayList;

public class MenuTest {
    ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
    ArrayList<Menu> menuList = new ArrayList<Menu>();
    public static void main(String[] args) {
        MenuTest test = new MenuTest();
        test.myMain();
    }

    void myMain() {
        readMenuTest();
        printMenuTest();
    }

    void readMenuTest() {
        try {
            File file = new File(Ingredient.ingredientPath);
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                ingredientList.add(new Ingredient(filein));
            }
            filein.close();

            file = new File(Menu.menuPath);
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
