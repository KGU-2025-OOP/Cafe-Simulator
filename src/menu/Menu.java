package menu;

import java.util.Scanner;
import java.util.ArrayList;

public class Menu {
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

    ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    void addOptions(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        this.ingredientCount = this.ingredients.size();
    }

    Menu getMenu() {
        return this;
    }
}
