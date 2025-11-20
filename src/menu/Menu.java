package menu;

import java.util.Scanner;
import java.util.ArrayList;

public class Menu {
    public static final String menuPath = "resources/menu.txt";
    String name;
    int ingredientCount;
    ArrayList<MenuItem> items;  // Ingredient와 Option을 모두 담을 수 있도록 변경
    int price;

    public void readMenu(Scanner scanner, ArrayList<Ingredient> ingredientList) {
        this.name = scanner.next();
        this.ingredientCount = scanner.nextInt();
        this.items = new ArrayList<MenuItem>();
        for (int i = 0; i < this.ingredientCount; i++) {
            String ingredientName = scanner.next();
            for (Ingredient ingredient : ingredientList) {
                MenuItem matchedItem = ingredient.matches("재료", ingredientName);
                if (matchedItem != null) {
                    this.items.add(matchedItem);
                    break;
                }
            }
        }
        if (this.items.size() != ingredientCount) {
            throw new IllegalArgumentException("Ingredient not found for menu: " + this.name + " only found " + this.items.size() + " ingredients.");
        }
        this.price = scanner.nextInt();
    }


    public ArrayList<MenuItem> getItems() {
        return this.items;
    }

    // 하위 호환성을 위한 메서드
    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (MenuItem item : this.items) {
            if (item instanceof Ingredient) {
                ingredients.add((Ingredient) item);
            }
        }
        return ingredients;
    }

    // 하위 호환성을 위한 메서드
    public void addOptions(Ingredient ingredient) {
        this.items.add(ingredient);
        this.ingredientCount = this.items.size();
    }

    public Menu getMenu() {
        return this;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalCost() {
        int totalCost = 0;
        for (MenuItem item : items) {
            totalCost += item.getPrice();
        }
        return totalCost + this.price;  // 메뉴 가격 + 재료 가격
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
