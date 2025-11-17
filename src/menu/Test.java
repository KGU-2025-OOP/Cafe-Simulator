package menu;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test {
    ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
    ArrayList<Menu> menuList = new ArrayList<Menu>();
    ArrayList<Option> optionList = new ArrayList<Option>();
    public static void main(String[] args) {
        Test test = new Test();
        test.myMain();
    }

    void myMain() {
        optionList = new OptionList().getOptionList();
        ingredientList = new IngredientList().getIngredientList();
        menuList = new MenuList(ingredientList).getMenuList();
        printMenuTest();
    }

    void printMenuTest() {
        for (Menu menu : menuList) {
            System.out.println("Menu Name: " + menu.name);
            System.out.println("Ingredients:");
            for (Ingredient ingredient : menu.getIngredients()) {
                System.out.println(" - " + ingredient.name);
            }
            System.out.println("Price: " + menu.price);
            System.out.println();
        }
        for (Option option : optionList) {
            System.out.println("Option Name: " + option.name);
            System.out.println("Making Time: " + option.makingTime);
            System.out.println("Max Wait Time: " + option.maxWaitTime);
            System.out.println("Price: " + option.price);
            System.out.println();
        }
        System.out.println("Menu print test completed.");
    }
}
