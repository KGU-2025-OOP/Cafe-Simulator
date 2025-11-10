package menu;

import java.util.Scanner;

public class Ingredient {
    String type;
    String name;
    int makingTime;
    int maxWaitTime;
    int price;

    public Ingredient(Scanner scan) {
        this.type = scan.next();
        this.name = scan.next();
        this.makingTime = scan.nextInt();
        this.maxWaitTime = scan.nextInt();
        this.price = scan.nextInt();
    }

    Ingredient matches(String type, String name) {
        if (this.type.equals(type) && this.name.equals(name)) {
            return this;
        }
        return null;
    }
}
