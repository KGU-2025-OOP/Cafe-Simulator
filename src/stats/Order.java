package stats;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<OrderItem> items;

    public Order() {
        items = new ArrayList<>();
    }

    /**
     * 주문에 새 항목 추가.
     *
     * @param product 주문한 메뉴
     * @param quantity 수량
     */
    public void addItem(Product product, int quantity) {
        // 같은 메뉴 여러 번 추가되면 합치는 로직을 나중에 넣어도 된다.
        items.add(new OrderItem(product, quantity));
    }

    /** 주문 안의 항목 목록을 복사해서 돌려줌 */
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    /** 주문 총액 */
    public int getTotalPrice() {
        int sum = 0;
        for (OrderItem item : items) {
            sum += item.getSubtotal();
        }
        return sum;
    }

    /** 주문 비어 있는지 여부 */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Order]\n");
        for (OrderItem item : items) {
            sb.append("  ").append(item).append("\n");
        }
        sb.append("총액: ").append(getTotalPrice()).append("원\n");
        return sb.toString();
    }
}