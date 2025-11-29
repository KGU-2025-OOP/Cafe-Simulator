package stats;

public class GameSaveInfo {

    private final String cafeName;
    private final int day;

    public GameSaveInfo(String cafeName, int day) {
        this.cafeName = cafeName;
        this.day = day;
    }

    public String getCafeName() {
        return cafeName;
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "GameSaveInfo{" +
                "cafeName='" + cafeName + '\'' +
                ", day=" + day +
                '}';
    }
}