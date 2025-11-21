package order;

import menu.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    // 개별 메뉴 주문 항목
    public static class OrderItem {
        private Menu menu;                 // 메뉴
        private List<String> options;      // 옵션 리스트
        private int price;                 // 메뉴 + 옵션 가격

        public OrderItem(Menu menu) {
            this.menu = menu;
            this.options = new ArrayList<>();
            this.price = menu.getPrice();
        }

        public void addOption(String optionName, int optionPrice) {
            options.add(optionName);
            price += optionPrice;
        }

        public Menu getMenu() {
            return menu;
        }

        public List<String> getOptions() {
            return options;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(menu.getName())
                    .append(" (").append(menu.getPrice()).append("원)");

            if (!options.isEmpty()) {
                sb.append("\n  옵션:");
                for (String opt : options) {
                    sb.append("\n    - ").append(opt);
                }
            }

            sb.append("\n  가격: ").append(price).append("원\n");
            return sb.toString();
        }
    }

    private List<OrderItem> items;     // 여러 개의 메뉴
    private int totalPrice;

    public Order() {
        this.items = new ArrayList<>();
        this.totalPrice = 0;
    }

    // 메뉴 1개 추가
    public OrderItem addMenu(Menu menu) {
        OrderItem item = new OrderItem(menu);
        items.add(item);
        totalPrice += menu.getPrice();
        return item;
    }

    // 총 가격 업데이트
    public void addToTotal(int amount) {
        totalPrice += amount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    // 영수증 출력
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------ 영수증 ------\n");

        for (OrderItem item : items) {
            sb.append(item.toString()).append("\n");
        }

        sb.append("총 결제 금액: ").append(totalPrice).append("원\n");
        sb.append("-------------------\n");

        return sb.toString();
    }
}