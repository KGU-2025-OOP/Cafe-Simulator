package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.GameCanvas;

public class GameplayPanel extends JPanel {

    //private JLabel dayLabel;
    private JButton endDayButton;

    private GameCanvas gameCanvas;
    private Thread gameThread;

    public GameplayPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //JPanel topPanel = createTopBar();
        //add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

//    private JPanel createTopBar() {
//        JPanel topBarPanel = new JPanel(new BorderLayout());
//        topBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        topBarPanel.setBackground(Color.WHITE);
//
//        dayLabel = new JLabel("1일차");
//        dayLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
//
//        endDayButton = new JButton("하루 마감 (임시)");
//        endDayButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
//
//        topBarPanel.add(dayLabel, BorderLayout.WEST);
//        topBarPanel.add(endDayButton, BorderLayout.CENTER);
//
//        return topBarPanel;
//    }
//
//    public JButton getEndDayButton() {
//        return endDayButton;
//    }
//
//    public void setDayLabel(String text) {
//        dayLabel.setText(text);
//    }

    public void startGame() {
        if (gameCanvas != null) {
            gameCanvas.shouldRun = true;
            gameThread = new Thread(gameCanvas);
            gameThread.start();
            gameCanvas.requestFocus();
        }
    }

    public void stopGame() {
        if (gameCanvas != null) {
            gameCanvas.shouldRun = false;
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        gameCanvas = new GameCanvas(
                ScreenConfig.WIDTH,
                ScreenConfig.HEIGHT
        );
        mainPanel.add(gameCanvas, BorderLayout.CENTER);

        return mainPanel;
    }
}