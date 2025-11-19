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

public class GameScreen extends JPanel
{
    private JLabel m_dayLabel;
    private JLabel m_timeLabel;
    private JButton m_endDayButton;
    private GameCanvas m_gameCanvas;

    public GameScreen()
    {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = CreateTopBar();
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = CreateMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    public class GameCanvas extends JPanel
    {
        public GameCanvas()
        {
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        @Override
        protected void paintComponent(Graphics g)
        {
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

    private JPanel CreateTopBar()
    {
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        topBarPanel.setBackground(Color.WHITE);

        m_dayLabel = new JLabel("1일차");
        m_dayLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        m_timeLabel = new JLabel("마감까지 00:30");
        m_timeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        m_endDayButton = new JButton("하루 마감 (임시)");
        m_endDayButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        topBarPanel.add(m_dayLabel, BorderLayout.WEST);
        topBarPanel.add(m_endDayButton, BorderLayout.CENTER);
        topBarPanel.add(m_timeLabel, BorderLayout.EAST);

        return topBarPanel;
    }

    public JButton GetEndDayButton()
    {
        return m_endDayButton;
    }

    public void SetDayLabel(String text)
    {
        m_dayLabel.setText(text);
    }

    public void RepaintCanvas()
    {
        if (m_gameCanvas != null)
        {
            m_gameCanvas.repaint();
        }
    }

    private JPanel CreateMainPanel()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topPathPanel = CreateTopPathPanel();
        JPanel bottomPathPanel = CreateBottomPathPanel();
        JPanel leftPathPanel = CreateLeftPathPanel();
        JPanel rightPathPanel = CreateRightPathPanel();

        topPathPanel.setPreferredSize(new Dimension(0, 100));
        bottomPathPanel.setPreferredSize(new Dimension(0, 100));
        leftPathPanel.setPreferredSize(new Dimension(100, 0));
        rightPathPanel.setPreferredSize(new Dimension(100, 0));

        mainPanel.add(topPathPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPathPanel, BorderLayout.SOUTH);
        mainPanel.add(leftPathPanel, BorderLayout.WEST);
        mainPanel.add(rightPathPanel, BorderLayout.EAST);

        m_gameCanvas = new GameCanvas();
        mainPanel.add(m_gameCanvas, BorderLayout.CENTER);

        return mainPanel;
    }

    private void CreateCustomerPath()
    {
        add(CreateTopPathPanel(), BorderLayout.PAGE_START);
        add(CreateBottomPathPanel(), BorderLayout.PAGE_END);
        add(CreateLeftPathPanel(), BorderLayout.LINE_START);
        add(CreateRightPathPanel(), BorderLayout.LINE_END);
    }

    private JPanel CreateTopPathPanel()
    {
        JPanel topPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPathPanel.setBackground(Color.RED);

        return topPathPanel;
    }

    private JPanel CreateBottomPathPanel()
    {
        JPanel bottomPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        bottomPathPanel.setBackground(Color.GREEN);

        return bottomPathPanel;
    }

    private JPanel CreateLeftPathPanel()
    {
        JPanel leftPathPanel = new JPanel();
        leftPathPanel.setLayout(new BoxLayout(leftPathPanel, BoxLayout.Y_AXIS));
        leftPathPanel.setBackground(Color.BLUE);

        return leftPathPanel;
    }

    private JPanel CreateRightPathPanel()
    {
        JPanel rightPathPanel = new JPanel();
        rightPathPanel.setLayout(new BoxLayout(rightPathPanel, BoxLayout.Y_AXIS));
        rightPathPanel.setBackground(Color.YELLOW);

        return rightPathPanel;
    }
}