package stats;

public class DailyRevenueRecord {

    private final int round;
    private final int revenue;

    public DailyRevenueRecord(int round, int revenue) {
        this.round = round;
        this.revenue = revenue;
    }

    public int getRound() {
        return round;
    }

    public int getRevenue() {
        return revenue;
    }

    @Override
    public String toString() {
        return "DailyRevenueRecord{" +
                "round=" + round +
                ", revenue=" + revenue +
                '}';
    }
}