package stats;


public abstract class Product implements Preparable {
    private String name;
    private int price;
    private int prepTime;

    public Product(String name, int price, int prepTime) {
        this.name = name;
        this.price = price;
        this.prepTime = prepTime;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getPrepTime() {
        return prepTime;
    }

    @Override
    public String toString() {
        return name + " (" + price + "원)";
    }
}