package stats;

public class GameSaveInfo {

    private final String cafeName;
    private final int level;

    public GameSaveInfo(String cafeName, int level) {
        this.cafeName = cafeName;
        this.level = level;
    }

    public String getCafeName() {
        return cafeName;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "GameSaveInfo{" +
                "cafeName='" + cafeName + '\'' +
                ", level=" + level +
                '}';
    }
}