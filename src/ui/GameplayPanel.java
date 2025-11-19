package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameplayPanel extends JPanel {
	
	private JLabel dayLabel;
    private JLabel timeLabel;
    private JButton endDayButton;
    private GameCanvas gameCanvas;

    public GameplayPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = createTopBar();
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    public class GameCanvas extends JPanel {
        public GameCanvas() {
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            g.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            g.drawString("여기가 게임 캔버스 영역입니다.", 50, 50);

            g.setColor(Color.ORANGE);
            g.fillRect(50, 80, 100, 100);

            g.setColor(Color.BLUE);
            g.drawOval(170, 80, 100, 100);
        }
    }

    private JPanel createTopBar() {
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        topBarPanel.setBackground(Color.WHITE);

        dayLabel = new JLabel("1일차");
        dayLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        timeLabel = new JLabel("마감까지 00:30");
        timeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        endDayButton = new JButton("하루 마감 (임시)");
        endDayButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        topBarPanel.add(dayLabel, BorderLayout.WEST);
        topBarPanel.add(endDayButton, BorderLayout.CENTER);
        topBarPanel.add(timeLabel, BorderLayout.EAST);

        return topBarPanel;
    }

    public JButton getEndDayButton() {
        return endDayButton;
    }

    public void setDayLabel(String text) {
        dayLabel.setText(text);
    }

    public void repaintCanvas() {
        if (gameCanvas != null) {
            gameCanvas.repaint();
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topPathPanel = createTopPathPanel();
        JPanel bottomPathPanel = createBottomPathPanel();
        JPanel leftPathPanel = createLeftPathPanel();
        JPanel rightPathPanel = createRightPathPanel();

        topPathPanel.setPreferredSize(new Dimension(0, 100));
        bottomPathPanel.setPreferredSize(new Dimension(0, 100));
        leftPathPanel.setPreferredSize(new Dimension(100, 0));
        rightPathPanel.setPreferredSize(new Dimension(100, 0));

        mainPanel.add(topPathPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPathPanel, BorderLayout.SOUTH);
        mainPanel.add(leftPathPanel, BorderLayout.WEST);
        mainPanel.add(rightPathPanel, BorderLayout.EAST);

        gameCanvas = new GameCanvas();
        mainPanel.add(gameCanvas, BorderLayout.CENTER);

        return mainPanel;
    }

    // 삭제? 안쓰임
    private void createCustomerPath() {
        add(createTopPathPanel(), BorderLayout.PAGE_START);
        add(createBottomPathPanel(), BorderLayout.PAGE_END);
        add(createLeftPathPanel(), BorderLayout.LINE_START);
        add(createRightPathPanel(), BorderLayout.LINE_END);
    }

    private JPanel createTopPathPanel() {
        JPanel topPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPathPanel.setBackground(Color.RED);

        return topPathPanel;
    }

    private JPanel createBottomPathPanel() {
        JPanel bottomPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        bottomPathPanel.setBackground(Color.GREEN);

        return bottomPathPanel;
    }

    private JPanel createLeftPathPanel() {
        JPanel leftPathPanel = new JPanel();
        leftPathPanel.setLayout(new BoxLayout(leftPathPanel, BoxLayout.Y_AXIS));
        leftPathPanel.setBackground(Color.BLUE);

        return leftPathPanel;
    }

    private JPanel createRightPathPanel() {
        JPanel rightPathPanel = new JPanel();
        rightPathPanel.setLayout(new BoxLayout(rightPathPanel, BoxLayout.Y_AXIS));
        rightPathPanel.setBackground(Color.YELLOW);

        return rightPathPanel;
    }
}