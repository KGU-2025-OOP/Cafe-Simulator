package menu;

import java.util.Scanner;

public class Option {
    String name;
    int makingTime;
    int maxWaitTime;
    int price;

    public Option(Scanner scan) {
        this.name = scan.next();
        this.makingTime = scan.nextInt();
        this.maxWaitTime = scan.nextInt();
        this.price = scan.nextInt();
    }

    Option matches(String type, String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }
}
