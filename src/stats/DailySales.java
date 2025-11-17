package stats;

import java.util.HashMap;
import java.util.Map;

public class DailySales {
    private int day;            // 게임 n일차
    private int totalRevenue;   // 그 날 총 매출
    private int totalOrders;    // 그 날 주문 수
    private int totalItems;     // 그 날 판매된 총 수량

    // 메뉴 이름 -> ProductStats
    private Map<String, ProductStats> productStatsMap;

    public DailySales(int day) {
        this.day = day;
        this.productStatsMap = new HashMap<>();
    }

    public int getDay() {
        return day;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Map<String, ProductStats> getProductStatsMap() {
        return productStatsMap;
    }

    /**
     * 주문 1건을 이 날 통계에 반영.
     */
    public void addOrder(Order order) {
        if (order == null || order.isEmpty()) return;

        totalOrders++;
        int orderTotal = order.getTotalPrice();
        totalRevenue += orderTotal;

        for (OrderItem item : order.getItems()) {
            String name = item.getProduct().getName();
            int price = item.getProduct().getPrice();
            int qty = item.getQuantity();

            totalItems += qty;

            ProductStats stats = productStatsMap.get(name);
            if (stats == null) {
                stats = new ProductStats(name);
                productStatsMap.put(name, stats);
            }
            stats.addSale(qty, price);
        }
    }

    /**
     * 이 날 기준 가장 많이 팔린 메뉴 정보(동률이면 아무거나 한 개)
     */
    public ProductStats getBestSellingProduct() {
        ProductStats best = null;
        int max = -1;
        for (ProductStats stats : productStatsMap.values()) {
            if (stats.getTotalQuantity() > max) {
                max = stats.getTotalQuantity();
                best = stats;
            }
        }
        return best;
    }

    /**
     * 이 날 주문 1건당 평균 매출(객단가)
     */
    public double getAverageOrderValue() {
        if (totalOrders == 0) return 0.0;
        return (double) totalRevenue / totalOrders;
    }
}