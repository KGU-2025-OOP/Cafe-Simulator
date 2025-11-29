package stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
    
    public int getUniqueCustomerCount() {
        Set<String> uniqueIds = new HashSet<>();
        for (SalesRecord r : salesRecords) {
            uniqueIds.add(r.getOrderId());   // 중복 제거
        }
        return uniqueIds.size();
    }

    public double getAverageOrderPrice() {
        if (salesRecords.isEmpty()) return 0.0;
        return (double) getTotalRevenueFromSales() / salesRecords.size();
    }

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

    public List<MenuStatRow> getAllMenuStatsByRoundAndMenu() {
        Map<String, int[]> agg = new HashMap<>();
        for (SalesRecord r : salesRecords) {
            String key = r.getRound() + "||" + r.getMenuName();
            int[] arr = agg.get(key);
            if (arr == null) {
                arr = new int[]{0, 0};
                agg.put(key, arr);
            }
            arr[0] += 1;
            arr[1] += r.getPrice();
        }
        List<MenuStatRow> result = new ArrayList<>();
        for (Map.Entry<String, int[]> e : agg.entrySet()) {
            String key = e.getKey();
            int idx = key.indexOf("||");
            int round = Integer.parseInt(key.substring(0, idx));
            String menu = key.substring(idx + 2);
            int quantity = e.getValue()[0];
            int total = e.getValue()[1];
            result.add(new MenuStatRow(round, menu, quantity, total));
        }
        result.sort((a, b) -> {
            if (a.getRound() != b.getRound()) {
                return Integer.compare(a.getRound(), b.getRound());
            }
            return a.getMenuName().compareTo(b.getMenuName());
        });
        return result;
    }

    public List<MenuStatRow> searchMenuStatsByMenuKeyword(String keyword) {
        String lower = keyword.toLowerCase(Locale.ROOT);
        List<MenuStatRow> all = getAllMenuStatsByRoundAndMenu();
        List<MenuStatRow> result = new ArrayList<>();
        for (MenuStatRow row : all) {
            if (row.getMenuName().toLowerCase(Locale.ROOT).contains(lower)) {
                result.add(row);
            }
        }
        return result;
    }

    public List<MenuStatRow> searchMenuStatsByRound(int round) {
        List<MenuStatRow> all = getAllMenuStatsByRoundAndMenu();
        List<MenuStatRow> result = new ArrayList<>();
        for (MenuStatRow row : all) {
            if (row.getRound() == round) {
                result.add(row);
            }
        }
        return result;
    }

    public List<OptionStatRow> getAllOptionStatsByRoundAndOption() {
        Map<String, int[]> agg = new HashMap<>();
        for (SalesRecord r : salesRecords) {
            List<String> options = r.getOptions();
            if (options == null) continue;
            for (String opt : options) {
                if (opt == null) continue;
                String trimmed = opt.trim();
                if (trimmed.isEmpty()) continue;
                String key = r.getRound() + "||" + trimmed;
                int[] arr = agg.get(key);
                if (arr == null) {
                    arr = new int[]{0, 0};
                    agg.put(key, arr);
                }
                arr[0] += 1;
                arr[1] += r.getPrice();
            }
        }
        List<OptionStatRow> result = new ArrayList<>();
        for (Map.Entry<String, int[]> e : agg.entrySet()) {
            String key = e.getKey();
            int idx = key.indexOf("||");
            int round = Integer.parseInt(key.substring(0, idx));
            String optionName = key.substring(idx + 2);
            int quantity = e.getValue()[0];
            int total = e.getValue()[1];
            result.add(new OptionStatRow(round, optionName, quantity, total));
        }
        result.sort((a, b) -> {
            if (a.getRound() != b.getRound()) {
                return Integer.compare(a.getRound(), b.getRound());
            }
            return a.getOptionName().compareTo(b.getOptionName());
        });
        return result;
    }

    public List<OptionStatRow> searchOptionStatsByOptionKeyword(String keyword) {
        String lower = keyword.toLowerCase(Locale.ROOT);
        List<OptionStatRow> all = getAllOptionStatsByRoundAndOption();
        List<OptionStatRow> result = new ArrayList<>();
        for (OptionStatRow row : all) {
            if (row.getOptionName().toLowerCase(Locale.ROOT).contains(lower)) {
                result.add(row);
            }
        }
        return result;
    }

    public List<OptionStatRow> searchOptionStatsByRound(int round) {
        List<OptionStatRow> all = getAllOptionStatsByRoundAndOption();
        List<OptionStatRow> result = new ArrayList<>();
        for (OptionStatRow row : all) {
            if (row.getRound() == round) {
                result.add(row);
            }
        }
        return result;
    }
}