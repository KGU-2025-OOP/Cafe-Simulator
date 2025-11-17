package stats;


public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /** 주문된 메뉴(상품) */
    public Product getProduct() {
        return product;
    }

    /** 주문 수량 */
    public int getQuantity() {
        return quantity;
    }

    /** 이 항목의 매출(= 단가 * 수량) */
    public int getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x " + quantity + " = "
                + getSubtotal() + "원";
    }
}