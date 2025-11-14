package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameScreen extends JPanel
{
    private JLabel dayLabel;
    private JLabel timeLabel;
    private JButton endDayButton;

    public GameScreen()
    {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        JPanel topPanel = this.createTopBar();
        this.add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = this.createMainPanel();
        this.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTopBar()
    {
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        topBarPanel.setBackground(Color.WHITE);

        this.dayLabel = new JLabel("1일차");
        this.dayLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        this.timeLabel = new JLabel("마감까지 00:30");
        this.timeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        this.endDayButton = new JButton("하루 마감 (임시)");
        this.endDayButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        topBarPanel.add(this.dayLabel, BorderLayout.WEST);
        topBarPanel.add(this.endDayButton, BorderLayout.CENTER);
        topBarPanel.add(this.timeLabel, BorderLayout.EAST);

        return topBarPanel;
    }

    public JButton getEndDayButton()
    {
        return this.endDayButton;
    }

    public void setDayLabel(String text)
    {
        this.dayLabel.setText(text);
    }

    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topPathPanel = this.createTopPathPanel();
        JPanel bottomPathPanel = this.createBottomPathPanel();
        JPanel leftPathPanel = this.createLeftPathPanel();
        JPanel rightPathPanel = this.createRightPathPanel();

        topPathPanel.setPreferredSize(new Dimension(0, 100));
        bottomPathPanel.setPreferredSize(new Dimension(0, 100));
        leftPathPanel.setPreferredSize(new Dimension(100, 0));
        rightPathPanel.setPreferredSize(new Dimension(100, 0));

        mainPanel.add(topPathPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPathPanel, BorderLayout.SOUTH);
        mainPanel.add(leftPathPanel, BorderLayout.WEST);
        mainPanel.add(rightPathPanel, BorderLayout.EAST);

        JPanel gameAreaPanel = this.createGameAreaPanel();
        mainPanel.add(gameAreaPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createGameAreaPanel()
    {
        JPanel gameAreaPanel = new JPanel();
        gameAreaPanel.setBackground(Color.GRAY);
        gameAreaPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JLabel centerLabel = new JLabel("게임 화면");
        gameAreaPanel.add(centerLabel);

        return gameAreaPanel;
    }

    private void createCustomerPath()
    {
        this.add(this.createTopPathPanel(), BorderLayout.PAGE_START);
        this.add(this.createBottomPathPanel(), BorderLayout.PAGE_END);
        this.add(this.createLeftPathPanel(), BorderLayout.LINE_START);
        this.add(this.createRightPathPanel(), BorderLayout.LINE_END);
    }

    private JPanel createTopPathPanel()
    {
        JPanel topPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPathPanel.setBackground(Color.RED);

        return topPathPanel;
    }

    private JPanel createBottomPathPanel()
    {
        JPanel bottomPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        bottomPathPanel.setBackground(Color.GREEN);

        return bottomPathPanel;
    }

    private JPanel createLeftPathPanel()
    {
        JPanel leftPathPanel = new JPanel();
        leftPathPanel.setLayout(new BoxLayout(leftPathPanel, BoxLayout.Y_AXIS));
        leftPathPanel.setBackground(Color.BLUE);

        return leftPathPanel;
    }

    private JPanel createRightPathPanel()
    {
        JPanel rightPathPanel = new JPanel();
        rightPathPanel.setLayout(new BoxLayout(rightPathPanel, BoxLayout.Y_AXIS));
        rightPathPanel.setBackground(Color.YELLOW);

        return rightPathPanel;
    }
}