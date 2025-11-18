package order;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private List<Order> orderHistory;   // 게임 전체 주문 기록
    private int totalSales;             // 누적 매출

    public OrderManager() {
        this.orderHistory = new ArrayList<>();
        this.totalSales = 0;
    }

    // 주문 1개 완료 후 저장
    public void addOrder(Order order) {
        orderHistory.add(order);
        totalSales += order.getTotalPrice();
    }

    // 매출 조회
    public int getTotalSales() {
        return totalSales;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    // 전체 영수증 출력
    public String printAllReceipts() {
        StringBuilder sb = new StringBuilder();
        sb.append("====== 모든 주문 내역 ======\n");

        for (Order o : orderHistory) {
            sb.append(o.toString()).append("\n");
        }

        sb.append("총 매출: ").append(totalSales).append("원\n");

        return sb.toString();
    }
}