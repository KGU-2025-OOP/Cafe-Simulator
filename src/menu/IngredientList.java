package menu;

import java.util.ArrayList;

public class IngredientList extends MenuItemList<Ingredient> {
    public static final String INGREDIENTS_PATH = "resources/ingredients.txt";
    public IngredientList() {
        super(INGREDIENTS_PATH, Ingredient::new);
    }

    public ArrayList<Ingredient> getIngredientList() {
        return getItemList();
    }
}
