package menu;

import java.util.Scanner;
import java.util.ArrayList;

public class Menu {
    public static final String menuPath = "resources/menu.txt";
    String name;
    int ingredientCount;
    ArrayList<Ingredient> ingredients;
    int price;

    public void readMenu(Scanner scanner, ArrayList<Ingredient> ingredientList) {
        this.name = scanner.next();
        this.ingredientCount = scanner.nextInt();
        this.ingredients = new ArrayList<Ingredient>();
        for (int i = 0; i < this.ingredientCount; i++) {
            String ingredientName = scanner.next();
            for (Ingredient ingredient : ingredientList) {
                Ingredient matchedIngredient = ingredient.matches("재료", ingredientName);
                if (matchedIngredient != null) {
                    this.ingredients.add(matchedIngredient);
                    break;
                }
            }
        }
        if (this.ingredients.size() != ingredientCount) {
            throw new IllegalArgumentException("Ingredient not found for menu: " + this.name + "only found " + this.ingredients.size() + " ingredients.");
        }
        this.price = scanner.nextInt();
    }

    public ArrayList<Ingredient> getIngredients() {
        return new ArrayList<Ingredient>(ingredients);
    }

    public void printMenu() {
        System.out.print("Menu Name: " + this.name + ", Ingredients: ");
        for (Ingredient ingredient : ingredients) {
            System.out.print(ingredient.name + " ");
        }
        System.out.println(", Price: " + this.price);
    }
}
