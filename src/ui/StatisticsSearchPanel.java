package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StatisticsSearchPanel extends JPanel {

    private JPanel contentContainer;
    private JButton backButton;
    private Image backgroundImage;

    private JTextField searchField;
    private JButton searchButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public StatisticsSearchPanel() {
        backgroundImage = ImageManager.getImage(ImageManager.IMG_MENU_BG);

        setLayout(new BorderLayout());
        setPreferredSize(ScreenConfig.FRAME_SIZE);
        setOpaque(false);

        JPanel backPanel = new JPanel(new GridBagLayout());
        backPanel.setOpaque(false);
        backPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        backButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_BACK));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backPanel.add(backButton);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("통계 검색      ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                String text = getText();
                FontMetrics fm = g2.getFontMetrics();

                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawString(text, x + 3, y + 3);

                g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);
            }
        };

        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setPreferredSize(new Dimension(250, 60));
        titlePanel.add(titleLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.setBorder(new EmptyBorder(20, 30, 0, 30));

        add(topPanel, BorderLayout.NORTH);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 30, 30, 30));
        contentPane.setOpaque(false);

        contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(true);
        contentContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentContainer.setBackground(new Color(255, 255, 255, 180));

        JPanel searchOuter = new JPanel(new GridBagLayout());
        searchOuter.setOpaque(false);

        JPanel searchBoxPanel = new JPanel(new BorderLayout());
        searchBoxPanel.setPreferredSize(new Dimension(450, 60));
        searchBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        searchBoxPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        searchField.setBorder(new EmptyBorder(0, 10, 0, 10));

        searchButton = new JButton("🔍");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.setContentAreaFilled(false);

        searchBoxPanel.add(searchField, BorderLayout.CENTER);
        searchBoxPanel.add(searchButton, BorderLayout.EAST);

        searchOuter.add(searchBoxPanel);
        contentContainer.add(searchOuter, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentContainer.add(centerPanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setOpaque(false);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setPreferredSize(new Dimension(260, 200));

        Font summaryFont = new Font("Malgun Gothic", Font.BOLD, 18);

        JLabel lbl1 = new JLabel("운영한 일수: ");
        JLabel lbl2 = new JLabel("총 수익금: ");
        JLabel lbl3 = new JLabel("하루당 평균 수익금: ");
        JLabel lbl4 = new JLabel("누적 방문객 수: ");

        lbl1.setFont(summaryFont);
        lbl2.setFont(summaryFont);
        lbl3.setFont(summaryFont);
        lbl4.setFont(summaryFont);

        summaryPanel.add(lbl1);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lbl2);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lbl3);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lbl4);

        centerPanel.add(summaryPanel, BorderLayout.WEST);

        String[] columnNames = {"순위", "메뉴", "판매지수", "판매금액"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFillsViewportHeight(true);
        resultTable.setRowHeight(24);
        resultTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        resultTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        contentPane.add(contentContainer, BorderLayout.CENTER);
        add(contentPane, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JPanel getContentPanePanel() {
        return contentContainer;
    }
}