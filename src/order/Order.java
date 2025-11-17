package order;

import menu.Menu;

import java.util.ArrayList;

public class Order {
    private ArrayList<Menu> list;

    // TODO: 주문 구현, 생성자 구현과 OrderManager에서 필요한 메서드 구현

    public int getLength() {
        return list.size();
    }

    public Menu getMenu(int idx) {
        return list.get(idx);
    }
}
