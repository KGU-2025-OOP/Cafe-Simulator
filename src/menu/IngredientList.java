package menu;

import java.util.ArrayList;

public class IngredientList extends MenuItemList<Ingredient> {
    
    public IngredientList() {
        super("ingredients.txt", Ingredient::new);
    }

    public ArrayList<Ingredient> getIngredientList() {
        return getItemList();
    }
}
