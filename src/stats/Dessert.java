package stats;

public class Dessert extends Product {

    public Dessert(String name, int price, int prepTime) {
        super(name, price, prepTime);
    }

    @Override
    public void prepare() {
        System.out.println("[Dessert] " + getName()
                + " 디저트를 준비합니다. (약 " + getPrepTime() + "초)");
    }
}