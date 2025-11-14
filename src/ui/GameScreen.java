package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton; // [추가]
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameScreen extends JPanel {

    private JLabel dayLabel;
    private JLabel timeLabel;
    private JButton endDayButton; // [신규] 하루 마감 임시 버튼

    public GameScreen() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = createTopBar();
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTopBar() {
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        topBarPanel.setBackground(Color.WHITE);

        dayLabel = new JLabel("1일차"); // (이 레이블은 GameFrame에서 동적으로 업데이트해야 합니다)
        dayLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        timeLabel = new JLabel("마감까지 00:30"); // (이 레이블도 타이머로 업데이트해야 합니다)
        timeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        // [신규] 임시 마감 버튼 추가
        endDayButton = new JButton("하루 마감 (임시)");
        endDayButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        topBarPanel.add(dayLabel, BorderLayout.WEST);
        topBarPanel.add(endDayButton, BorderLayout.CENTER); // 중앙에 버튼 추가
        topBarPanel.add(timeLabel, BorderLayout.EAST);

        return topBarPanel;
    }

    // [신규] GameFrame에서 버튼에 접근하기 위한 Getter
    public JButton getEndDayButton() {
        return this.endDayButton;
    }

    // (GameFrame에서 날짜 레이블을 업데이트하기 위한 Setter - 선택 사항)
    public void setDayLabel(String text) {
        this.dayLabel.setText(text);
    }

    // --- (이하 코드는 제공해주신 원본과 동일) ---

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

        JPanel gameAreaPanel = createGameAreaPanel();
        mainPanel.add(gameAreaPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createGameAreaPanel() {
        JPanel gameAreaPanel = new JPanel();
        gameAreaPanel.setBackground(Color.GRAY);
        gameAreaPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JLabel centerLabel = new JLabel("게임 화면");
        gameAreaPanel.add(centerLabel);

        return gameAreaPanel;
    }

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