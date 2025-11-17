package stats;

public class ProductStats {
    private String productName; // 메뉴 이름
    private int totalQuantity;  // 누적 판매 수량
    private int totalRevenue;   // 누적 매출(원)

    public ProductStats(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * 판매 기록 1건을 누적.
     *
     * @param quantity  판매된 수량
     * @param unitPrice 단가(원)
     */
    public void addSale(int quantity, int unitPrice) {
        this.totalQuantity += quantity;
        this.totalRevenue += quantity * unitPrice;
    }

    @Override
    public String toString() {
        return productName + " - 수량: " + totalQuantity
                + "개, 매출: " + totalRevenue + "원";
    }
}