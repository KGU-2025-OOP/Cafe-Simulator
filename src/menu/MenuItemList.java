package menu;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * MenuItem의 리스트를 파일에서 읽어오는 추상 클래스
 * 제네릭을 사용하여 Ingredient, Option 등 다양한 타입을 지원
 * 
 * @param <T> MenuItem을 상속받는 타입
 */
public abstract class MenuItemList<T extends MenuItem> {
    protected ArrayList<T> itemList;
    protected String fileName;

    /**
     * 생성자
     * @param fileName resources 폴더 내의 파일명
     * @param itemCreator Scanner를 받아 T 타입 객체를 생성하는 함수
     */
    public MenuItemList(String fileName, Function<Scanner, T> itemCreator) {
        this.fileName = fileName;
        this.itemList = new ArrayList<T>();
        loadFromFile(itemCreator);
    }

    private void loadFromFile(Function<Scanner, T> itemCreator) {
        try {
            java.io.File file = new java.io.File(fileName);
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                this.itemList.add(itemCreator.apply(filein));
            }
            filein.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public ArrayList<T> getItemList() {
        return this.itemList;
    }

    public T findByName(String name) {
        for (T item : itemList) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    // 전체 아이템 수 반환
    public int size() {
        return itemList.size();
    }

    // 인덱스로 아이템 반환
    public T get(int index) {
        return itemList.get(index);
    }
}
