package stats;

public class Drink extends Product {

    public Drink(String name, int price, int prepTime) {
        super(name, price, prepTime);
    }

    @Override
    public void prepare() {
        // 실제 게임에서는 이 부분을 애니메이션, 타이머 등과 연동할 수 있다.
        System.out.println("[Drink] " + getName()
                + " 음료를 준비합니다. (약 " + getPrepTime() + "초)");
    }
}