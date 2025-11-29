package stats;

public class MenuStatRow {

    private final int round;
    private final String menuName;
    private final int quantity;
    private final int totalRevenue;

    public MenuStatRow(int round, String menuName, int quantity, int totalRevenue) {
        this.round = round;
        this.menuName = menuName;
        this.quantity = quantity;
        this.totalRevenue = totalRevenue;
    }

    public int getRound() {
        return round;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public String toString() {
        return "MenuStatRow{" +
                "round=" + round +
                ", menuName='" + menuName + '\'' +
                ", quantity=" + quantity +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}