package menu;

import java.util.Scanner;
import java.util.ArrayList;


public class IngredientList {
    ArrayList<Ingredient> ingredientList;

    public IngredientList() {
        this.ingredientList = new ArrayList<Ingredient>();
        try {
            java.io.File file = new java.io.File("resources/ingredients.txt");
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                this.ingredientList.add(new Ingredient(filein));
            }
            filein.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }
    }

    public ArrayList<Ingredient> getIngredientList() {
        return this.ingredientList;
    }
}
