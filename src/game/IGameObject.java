package game;

import java.awt.Graphics2D;

public interface IGameObject {
    void draw(Graphics2D g);
    void update();
    float getDepth();
}
