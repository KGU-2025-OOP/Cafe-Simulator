package menu;

import java.util.Scanner;

/**
 * 재료와 옵션의 공통 속성과 동작을 정의하는 추상 클래스
 */
public abstract class MenuItem {
    protected String name;
    protected int makingTime;
    protected int maxWaitTime;
    protected int price;

    public MenuItem(Scanner scan) {
        this.name = scan.next();
        this.makingTime = scan.nextInt();
        this.maxWaitTime = scan.nextInt();
        this.price = scan.nextInt();
    }

    public MenuItem matches(String type, String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }

    // Getter 메서드들
    public String getName() {
        return name;
    }

    public int getMakingTime() {
        return makingTime;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public int getPrice() {
        return price;
    }
}
