package menu;

import java.util.Scanner;

/**
 * 재료와 옵션의 공통 속성과 동작을 정의하는 추상 클래스
 */
public abstract class MenuItem {
    protected String name;
    protected int makingTime;
    protected int maxWaitTime;
    protected int price;
    protected String imgPath; //이미지 경로
    protected java.io.File img; //이미지 파일

    public MenuItem(Scanner scan) {
        this.name = scan.next();
        this.makingTime = scan.nextInt();
        this.maxWaitTime = scan.nextInt();
        this.price = scan.nextInt();
        if (scan.hasNext()) {
            this.imgPath = scan.next();
            // 이미지 파일 로드 ===========================================
            java.io.File file = new java.io.File(this.imgPath);
            if (file.exists()) { // 이미지 파일이 존재하는지 확인
                this.img = file;
            } else {
                throw new IllegalArgumentException("Image file not found: " + this.imgPath);
            } // 이미지 파일 로드 ===========================================
        }
    }

    public MenuItem matches(String type, String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }

    // Getter 메서드들
    public String getName() {
        return name;
    }

    public int getMakingTime() {
        return makingTime;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public int getPrice() {
        return price;
    }

    public String getImgPath() {
        return imgPath;
    }
}
