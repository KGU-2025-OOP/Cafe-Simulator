package stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesRecord {

    private final int round;
    private final String timestamp;
    private final String orderId;
    private final int price;
    private final String menuName;
    private final List<String> options;

    public SalesRecord(int round,
                       String timestamp,
                       String orderId,
                       int price,
                       String menuName,
                       List<String> options) {
        this.round = round;
        this.timestamp = timestamp;
        this.orderId = orderId;
        this.price = price;
        this.menuName = menuName;
        this.options = new ArrayList<>(options);
    }

    public int getRound() {
        return round;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getPrice() {
        return price;
    }

    public String getMenuName() {
        return menuName;
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public String toString() {
        return "SalesRecord{" +
                "round=" + round +
                ", timestamp='" + timestamp + '\'' +
                ", orderId='" + orderId + '\'' +
                ", price=" + price +
                ", menuName='" + menuName + '\'' +
                ", options=" + options +
                '}';
    }
}
