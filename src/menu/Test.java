package menu;

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
        // 리스트 로드
        IngredientList ingredientListLoader = new IngredientList();
        OptionList optionListLoader = new OptionList();
        
        ingredientList = ingredientListLoader.getIngredientList();
        optionList = optionListLoader.getOptionList();
        menuList = new MenuList(ingredientList).getMenuList();
        
        printMenuTest();
        testNewFeatures();
    }

    void printMenuTest() {
        System.out.println("=== MENU TEST ===");
        for (Menu menu : menuList) {
            System.out.println("Menu Name: " + menu.getName());
            System.out.println("Ingredients:");
            for (MenuItem item : menu.getItems()) {
                System.out.println(" - " + item.getName() + " (Price: " + item.getPrice() + ")");
            }
            System.out.println("Menu Price: " + menu.getPrice());
            System.out.println("Total Cost: " + menu.getTotalCost() + " won");
            System.out.println();
        }
        
        System.out.println("=== OPTIONS ===");
        for (Option option : optionList) {
            System.out.println("Option Name: " + option.getName());
            System.out.println("Making Time: " + option.getMakingTime());
            System.out.println("Max Wait Time: " + option.getMaxWaitTime());
            System.out.println("Price: " + option.getPrice());
            System.out.println();
        }
        System.out.println("Menu print test completed.");
    }

    void testNewFeatures() {
        System.out.println("\n=== NEW FEATURES TEST ===");
        
        // 1. findByName 테스트
        IngredientList ingredientListLoader = new IngredientList();
        Ingredient tomato = ingredientListLoader.findByName("토마토");
        if (tomato != null) {
            System.out.println("Found ingredient: " + tomato.getName() + " (Price: " + tomato.getPrice() + ")");
        }
        
        // 2. MenuList의 findByName 테스트
        MenuList menuListLoader = new MenuList(ingredientList);
        Menu pizza = menuListLoader.findByName("피자");
        if (pizza != null) {
            System.out.println("Found menu: " + pizza.getName() + " (Price: " + pizza.getPrice() + ")");
        }
        
        // 3. size() 테스트
        System.out.println("Total ingredients: " + ingredientListLoader.size());
        System.out.println("Total menus: " + menuListLoader.size());
        
        // 4. 옵션을 메뉴에 추가하는 예제
        if (pizza != null && optionList.size() > 0) {
            Option firstOption = optionList.get(0);
            System.out.println("\nAdding option '" + firstOption.getName() + "' to pizza...");
            pizza.addMenuItem(firstOption);
            System.out.println("Pizza total items now: " + pizza.getItems().size());
        }
        
        System.out.println("\nNew features test completed.");
    }
}
