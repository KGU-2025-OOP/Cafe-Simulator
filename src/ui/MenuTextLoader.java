package ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MenuTextLoader {

    private static final String MENU_FILE_PATH = "/text/menu.txt";

    public static List<MenuItem> loadMenuData() {
        List<MenuItem> loadedItems = new ArrayList<>();
        InputStream is = MenuTextLoader.class.getResourceAsStream(MENU_FILE_PATH);

        if (is == null) {
            return loadedItems;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                try {
                    int typeCode = Integer.parseInt(parts[0]);
                    MenuItem.MenuType type = (typeCode == 1) ? MenuItem.MenuType.Coffee : MenuItem.MenuType.NonCoffee;
                    String name = parts[1];
                    int ingredientCount = Integer.parseInt(parts[2]);

                    List<String> ingredients = new ArrayList<>();
                    for (int i = 0; i < ingredientCount; i++) {
                        ingredients.add(parts[3 + i]);
                    }

                    int priceIndex = 3 + ingredientCount;
                    int price = Integer.parseInt(parts[priceIndex]);
                    String imageName = parts[priceIndex + 1];

                    MenuItem item = new MenuItem(name, type, true, price, ingredients, imageName);
                    loadedItems.add(item);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadedItems;
    }
}