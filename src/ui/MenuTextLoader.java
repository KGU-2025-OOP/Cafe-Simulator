package ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MenuTextLoader {

    private static final String MENU_FILE_PATH = "/ui/menu.txt";

    public static List<MenuItem> loadMenuData() {
        List<MenuItem> loadedItems = new ArrayList<>();

        InputStream is = MenuTextLoader.class.getResourceAsStream(MENU_FILE_PATH);

        if (is == null) {
            System.err.println("[에러] 메뉴 파일을 찾을 수 없습니다: " + MENU_FILE_PATH);
            return loadedItems;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            System.out.println("=== 메뉴 데이터 로딩 시작 ===");

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                try {
                    // 1. 타입 파싱 (1=Coffee, 0=NonCoffee)
                    int typeCode = Integer.parseInt(parts[0]);
                    MenuItem.MenuType type = (typeCode == 1) ? MenuItem.MenuType.Coffee : MenuItem.MenuType.NonCoffee;

                    // 2. 이름
                    String name = parts[1];

                    // 3. 재료 개수
                    int ingredientCount = Integer.parseInt(parts[2]);

                    // 4. 재료 리스트
                    List<String> ingredients = new ArrayList<>();
                    for (int i = 0; i < ingredientCount; i++) {
                        ingredients.add(parts[3 + i]);
                    }

                    // 5. 가격 및 이미지
                    int priceIndex = 3 + ingredientCount;
                    int price = Integer.parseInt(parts[priceIndex]);
                    String imageName = parts[priceIndex + 1];

                    MenuItem item = new MenuItem(name, type, true, price, ingredients, imageName);
                    loadedItems.add(item);

                    System.out.printf("로드됨([%s]): %s (재료 %d개, %d원)\n", type, name, ingredientCount, price);

                } catch (Exception e) {
                    System.err.println("파싱 오류 (라인 무시됨): " + line);
                }
            }
            System.out.println("=== 메뉴 데이터 로딩 완료 (총 " + loadedItems.size() + "개) ===");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadedItems;
    }
}