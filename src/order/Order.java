package order;

import menu.Menu;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Menu menuItem;             // 선택한 음료
    private List<String> options;      // 추가 옵션(휘핑, 디저트 등)
    private int totalPrice;            // 최종 가

    public Order(Menu menuItem) {
        this.menuItem = menuItem;
        this.options = new ArrayList<>();
        this.totalPrice = menuItem.getPrice();
    }

    public void addOption(String optionName, int optionPrice) {
        options.add(optionName);
        totalPrice += optionPrice;
    }

    public Menu getMenuItem() {
        return menuItem;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    // 영수증 1개 출력용
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------ 영수증 ------\n");
        sb.append("메뉴: ").append(menuItem.getName())
                .append(" / ").append(menuItem.getPrice()).append("원\n");

        if (!options.isEmpty()) {
            sb.append("옵션:\n");
            for (String opt : options) {
                sb.append("  - ").append(opt).append("\n");
            }
        }

        sb.append("-------------------\n");
        sb.append("총 결제 금액: ").append(totalPrice).append("원\n");

        return sb.toString();
    }
}