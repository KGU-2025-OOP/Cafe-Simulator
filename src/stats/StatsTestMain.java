package stats;
public class StatsTestMain {

    public static void main(String[] args) {
        mymain();
    }

    public static void mymain() {
        try {
            String salesFile = "data/판매 기록.txt";
            String dailyFile = "data/일일 매출.txt";
            String saveFile  = "data/게임 세이브_로드.txt";

            StatsService stats = StatsService.fromFiles(salesFile, dailyFile, saveFile);

            System.out.println("=== 전체 판매 기록 ===");
            for (SalesRecord r : stats.getAllSalesRecords()) {
                System.out.println(r);
            }

            System.out.println("\n=== 일일 매출 ===");
            for (DailyRevenueRecord dr : stats.getAllDailyRevenues()) {
                System.out.println(dr);
            }

            System.out.println("\n=== 세이브 정보 ===");
            System.out.println(stats.getGameSaveInfo());

            System.out.println("\n=== 통계 예시 ===");
            System.out.println("총 매출(판매 기록 기준): " + stats.getTotalRevenueFromSales());
            System.out.println("총 매출(일일 매출 기준): " + stats.getTotalRevenueFromDaily());
            System.out.println("총 주문 수: " + stats.getTotalOrderCount());
            System.out.println("평균 객단가: " + stats.getAverageOrderPrice());

            System.out.println("\nRound 1 매출(판매 기록 기준): " +
                    stats.getRevenueForRoundFromSales(1));
            System.out.println("Round 1 매출(일일 매출 기준): " +
                    stats.getRevenueForRoundFromDaily(1));

            System.out.println("\n베스트 메뉴: " + stats.getBestSellingMenuName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}