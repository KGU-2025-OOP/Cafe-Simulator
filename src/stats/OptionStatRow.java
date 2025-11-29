package stats;

public class OptionStatRow {

    private final int round;
    private final String optionName;
    private final int quantity;
    private final int totalRevenue;

    public OptionStatRow(int round, String optionName, int quantity, int totalRevenue) {
        this.round = round;
        this.optionName = optionName;
        this.quantity = quantity;
        this.totalRevenue = totalRevenue;
    }

    public int getRound() {
        return round;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public String toString() {
        return "OptionStatRow{" +
                "round=" + round +
                ", optionName='" + optionName + '\'' +
                ", quantity=" + quantity +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}