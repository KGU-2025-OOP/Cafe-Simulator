package stats;

import java.util.HashMap;
import java.util.Map;


public class SalesStatisticsManager {

    // day(1일차, 2일차, ...) -> DailySales
    private Map<Integer, DailySales> dailySalesMap;

    public SalesStatisticsManager() {
        dailySalesMap = new HashMap<>();
    }

    /** day에 해당하는 DailySales 객체를 가져오고, 없으면 새로 만들어 등록한다. */
    private DailySales getOrCreateDailySales(int day) {
        DailySales ds = dailySalesMap.get(day);
        if (ds == null) {
            ds = new DailySales(day);
             dailySalesMap.put(day, ds);
        }
        return ds;
    }

    /**
     * 특정 일자(day)의 주문 1건을 통계에 반영.
     * GUI에서 "결제 완료" 시점에 이 메서드를 딱 한 번 호출해 주면 된다.
     *
     * @param day   게임상의 n일차
     * @param order 결제 완료된 Order
     */
    public void recordOrder(int day, Order order) {
        if (order == null || order.isEmpty()) return;
        DailySales ds = getOrCreateDailySales(day);
        ds.addOrder(order);
    }

    /** 일자별 통계 객체 가져오기 (없으면 null) */
    public DailySales getDailySales(int day) {
        return dailySalesMap.get(day);
    }

    /** 전체 기간 총 매출 */
    public int getTotalRevenueAll() {
        int sum = 0;
        for (DailySales ds : dailySalesMap.values()) {
            sum += ds.getTotalRevenue();
        }
        return sum;
    }

    /** 전체 기간 총 주문 수 */
    public int getTotalOrdersAll() {
        int sum = 0;
        for (DailySales ds : dailySalesMap.values()) {
            sum += ds.getTotalOrders();
        }
        return sum;
    }

    /** 전체 기간 주문 1건당 평균 매출(객단가) */
    public double getAverageOrderValueAll() {
        int totalRevenue = getTotalRevenueAll();
        int totalOrders = getTotalOrdersAll();
        if (totalOrders == 0) return 0.0;
        return (double) totalRevenue / totalOrders;
    }

    /**
     * 전체 기간 메뉴별 통계를 모두 합쳐서 반환.
     * (그래프, 순위표 등에 사용)
     */
    public Map<String, ProductStats> getProductStatsAll() {
        Map<String, ProductStats> merged = new HashMap<>();

        for (DailySales ds : dailySalesMap.values()) {
            for (ProductStats ps : ds.getProductStatsMap().values()) {
                ProductStats agg = merged.get(ps.getProductName());
                if (agg == null) {
                    agg = new ProductStats(ps.getProductName());
                    merged.put(ps.getProductName(), agg);
                }
                int qty = ps.getTotalQuantity();
                int unitPrice = (qty == 0) ? 0 : ps.getTotalRevenue() / qty;
                agg.addSale(qty, unitPrice);
            }
        }
        return merged;
    }

    /** 전체 기간 기준 베스트셀러 메뉴 이름 (동률이면 아무거나) */
    public String getBestSellingProductAll() {
        Map<String, ProductStats> merged = getProductStatsAll();
        String bestName = null;
        int maxQty = -1;

        for (ProductStats stats : merged.values()) {
            if (stats.getTotalQuantity() > maxQty) {
                maxQty = stats.getTotalQuantity();
                bestName = stats.getProductName();
            }
        }
        return bestName;
    }

    /** 디버그용: 콘솔에 전체 통계 출력 */
    public void printAllStats() {
        System.out.println("=== 전체 일자 매출 통계 ===");
        for (Map.Entry<Integer, DailySales> e : dailySalesMap.entrySet()) {
            int day = e.getKey();
            DailySales ds = e.getValue();
            System.out.println("[Day " + day + "]");
            System.out.println("  총 매출: " + ds.getTotalRevenue() + "원");
            System.out.println("  총 주문 수: " + ds.getTotalOrders());
            System.out.println("  총 판매 수량: " + ds.getTotalItems() + "개");
            ProductStats best = ds.getBestSellingProduct();
            if (best != null) {
                System.out.println("  베스트 메뉴: " + best.getProductName()
                        + " (" + best.getTotalQuantity() + "개)");
            }
        }

        System.out.println("=== 전체 기간 요약 ===");
        System.out.println("전체 매출: " + getTotalRevenueAll() + "원");
        System.out.println("전체 주문 수: " + getTotalOrdersAll());
        System.out.println("전체 평균 객단가: " + getAverageOrderValueAll());

        String bestAll = getBestSellingProductAll();
        System.out.println("전체 기간 베스트 메뉴: " + bestAll);
    }
}