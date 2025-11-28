package stats;

import java.io.IOException;
import java.util.*;


public class StatsService {

    private final List<SalesRecord> salesRecords;
    private final List<DailyRevenueRecord> dailyRevenues;
    private final GameSaveInfo gameSaveInfo;

    public StatsService(List<SalesRecord> salesRecords,
                        List<DailyRevenueRecord> dailyRevenues,
                        GameSaveInfo gameSaveInfo) {
        this.salesRecords = new ArrayList<>(salesRecords);
        this.dailyRevenues = new ArrayList<>(dailyRevenues);
        this.gameSaveInfo = gameSaveInfo;
    }


    public static StatsService fromFiles(String salesFilePath,
                                         String dailyRevenueFilePath,
                                         String saveFilePath) throws IOException {

        List<SalesRecord> sales = StatsFileLoader.loadSalesRecords(salesFilePath);
        List<DailyRevenueRecord> daily = StatsFileLoader.loadDailyRevenues(dailyRevenueFilePath);
        GameSaveInfo saveInfo = StatsFileLoader.loadGameSaveInfo(saveFilePath);

        return new StatsService(sales, daily, saveInfo);
    }

    public List<SalesRecord> getAllSalesRecords() {
        return Collections.unmodifiableList(salesRecords);
    }

    public List<DailyRevenueRecord> getAllDailyRevenues() {
        return Collections.unmodifiableList(dailyRevenues);
    }

    public GameSaveInfo getGameSaveInfo() {
        return gameSaveInfo;
    }


    public int getTotalRevenueFromSales() {
        int sum = 0;
        for (SalesRecord r : salesRecords) {
            sum += r.getPrice();
        }
        return sum;
    }

    public int getTotalRevenueFromDaily() {
        int sum = 0;
        for (DailyRevenueRecord r : dailyRevenues) {
            sum += r.getRevenue();
        }
        return sum;
    }

    public int getRevenueForRoundFromSales(int round) {
        int sum = 0;
        for (SalesRecord r : salesRecords) {
            if (r.getRound() == round) {
                sum += r.getPrice();
            }
        }
        return sum;
    }

    public int getRevenueForRoundFromDaily(int round) {
        for (DailyRevenueRecord r : dailyRevenues) {
            if (r.getRound() == round) {
                return r.getRevenue();
            }
        }
        return 0;
    }

    public int getTotalOrderCount() {
        return salesRecords.size();
    }

    public double getAverageOrderPrice() {
        if (salesRecords.isEmpty()) return 0.0;
        return (double) getTotalRevenueFromSales() / salesRecords.size();
    }

    // ====== 검색 기능 ======

    public List<SalesRecord> findSalesByMenuName(String keyword) {
        String lower = keyword.toLowerCase(Locale.ROOT);
        List<SalesRecord> result = new ArrayList<>();
        for (SalesRecord r : salesRecords) {
            if (r.getMenuName().toLowerCase(Locale.ROOT).contains(lower)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<SalesRecord> findSalesByRound(int round) {
        List<SalesRecord> result = new ArrayList<>();
        for (SalesRecord r : salesRecords) {
            if (r.getRound() == round) {
                result.add(r);
            }
        }
        return result;
    }

    public List<SalesRecord> findSalesByOrderId(String orderId) {
        List<SalesRecord> result = new ArrayList<>();
        for (SalesRecord r : salesRecords) {
            if (r.getOrderId().equals(orderId)) {
                result.add(r);
            }
        }
        return result;
    }

    // ====== 정렬 기능 ======

    public List<SalesRecord> sortByPrice(boolean ascending) {
        List<SalesRecord> copy = new ArrayList<>(salesRecords);
        copy.sort((a, b) -> {
            int cmp = Integer.compare(a.getPrice(), b.getPrice());
            return ascending ? cmp : -cmp;
        });
        return copy;
    }

    public List<SalesRecord> sortByTimestamp(boolean ascending) {
        List<SalesRecord> copy = new ArrayList<>(salesRecords);
        copy.sort((a, b) -> {
            int cmp = a.getTimestamp().compareTo(b.getTimestamp());
            return ascending ? cmp : -cmp;
        });
        return copy;
    }

    // ====== 메뉴별 집계 ======

    public Map<String, Integer> getMenuOrderCountMap() {
        Map<String, Integer> map = new HashMap<>();
        for (SalesRecord r : salesRecords) {
            map.merge(r.getMenuName(), 1, Integer::sum);
        }
        return map;
    }

    public Map<String, Integer> getMenuRevenueMap() {
        Map<String, Integer> map = new HashMap<>();
        for (SalesRecord r : salesRecords) {
            map.merge(r.getMenuName(), r.getPrice(), Integer::sum);
        }
        return map;
    }

    public String getBestSellingMenuName() {
        Map<String, Integer> countMap = getMenuOrderCountMap();
        String best = null;
        int max = -1;
        for (Map.Entry<String, Integer> e : countMap.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                best = e.getKey();
            }
        }
        return best;
    }
}