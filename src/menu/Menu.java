package menu;

import entities.DeadLine;
import entities.DropItem;
import util.Vector2f;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Menu {
    private int isCoffee;
    private String name;
    private int ingredientCount;
    private ArrayList<MenuItem> items;
    private int price;
    protected String imgPath; //이미지 경로
    protected File img; //이미지 파일

    public void readMenu(Scanner scanner, ArrayList<Ingredient> ingredientList) {
        String line = scanner.nextLine();
        String[] tokens = line.split(" ");

        this.isCoffee = Integer.parseInt(tokens[0]);
        this.name = tokens[1];
        this.ingredientCount = Integer.parseInt(tokens[2]);
        this.items = new ArrayList<MenuItem>();
        for (int i = 0; i < this.ingredientCount; i++) {
            String ingredientName = tokens[3 + i];
            for (Ingredient ingredient : ingredientList) {
                MenuItem matchedItem = ingredient.matches("재료", ingredientName);
                if (matchedItem != null) {
                    this.items.add(matchedItem);
                    break;
                }
            }
        }
        if (this.items.size() != ingredientCount)
            throw new IllegalArgumentException("Ingredient not found for menu: " + this.name + " only found " + this.items.size() + " ingredients.");

        this.price = Integer.parseInt(tokens[3 + this.ingredientCount]);

        this.imgPath = tokens[4 + this.ingredientCount];
        // 이미지 파일 로드 ===========================================
        java.io.File file = new java.io.File(this.imgPath);
        if (file.exists()) { // 이미지 파일이 존재하는지 확인
            this.img = file;
        } else {
            throw new IllegalArgumentException("Image file not found: " + this.imgPath);
        }
        // 이미지 파일 로드 ===========================================
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

    public ArrayList<Option> getOptions() {
        ArrayList<Option> ingredients = new ArrayList<Option>();
        for (MenuItem item : this.items) {
            if (item instanceof Option) {
                ingredients.add((Option) item);
            }
        }
        return ingredients;
    }

    // 하위 호환성을 위한 메서드
    public void addOptions(final Option option) {
        this.items.add(option);
        price += option.price;
        this.ingredientCount = this.items.size();
    }

    public int getIsCoffee() {
        return isCoffee;
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
    public ArrayList<DropItem> getDrops(DeadLine deadline) {
        int tempDropSpeed = 15; // Ingredients의 처리 시간에 비례해서 값을 키울 에정이였음

        ArrayList<DropItem> res = new ArrayList<DropItem>();
        for (MenuItem item : items) {
            DropItem newDrop = new DropItem(new Vector2f(), new Vector2f(0, -1), tempDropSpeed,
                    item.getName(), DropItem.defaultFont,
                    item.getBufferedImage(), deadline);
            res.add(newDrop);
        }

        return res;
    }

    public File getImage() {
        return img;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(price);
        strBuf.append(" ");
        strBuf.append(name);
        var iter = getOptions();
        for (var i : iter) {
            strBuf.append(" ");
            strBuf.append(i.getName());

        }
        return strBuf.toString();
    }
}
