package ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

public class DaySummaryDialog extends JDialog {

    private static final Font FONT_TITLE   = new Font("Malgun Gothic", Font.BOLD, 28);
    private static final Font FONT_SUB     = new Font("Malgun Gothic", Font.PLAIN, 16);
    private static final Font FONT_LABEL   = new Font("Malgun Gothic", Font.PLAIN, 20);
    private static final Font FONT_VALUE   = new Font("Malgun Gothic", Font.BOLD, 20);
    private static final Font FONT_PROFIT  = new Font("Malgun Gothic", Font.BOLD, 28);
    private static final Font FONT_BUTTON  = new Font("Malgun Gothic", Font.BOLD, 18);

    private static final Color COLOR_PROFIT_GREEN = new Color(0, 140, 0);
    private static final Color COLOR_SUBTEXT      = new Color(120, 120, 120);
    private static final Color COLOR_CARD_BG      = new Color(255, 255, 255, 210);
    private static final Color COLOR_CARD_BORDER  = new Color(60, 40, 30, 200);
    private static final Color COLOR_BUTTON_BG    = new Color(160, 110, 80);
    private static final Color COLOR_BUTTON_BG_R  = new Color(200, 60, 60);

    private Image backgroundImage;

    public DaySummaryDialog(JFrame parent,
                            int dayNumber,
                            int customerCount,
                            int revenue,
                            long totalRevenue) {

        super(parent, dayNumber + "일차 결산", true);

        // 일단은 비용 계산은 안 들어와 있으니, 순수익 = 오늘 매출로만 계산
        int netProfit = revenue;

        // 배경 이미지 로드
        backgroundImage = ImageManager.getImage(ImageManager.IMG_MENU_BG);

        // === 1. 배경 패널 (전체) ===
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // === 2. 가운데 카드 패널 ===
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 30;
                int w = getWidth();
                int h = getHeight();

                // 카드 배경
                g2.setColor(COLOR_CARD_BG);
                g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);

                // 카드 테두리
                g2.setColor(COLOR_CARD_BORDER);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

                super.paintComponent(g);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        cardPanel.setPreferredSize(new Dimension(520, 420));

        // === 2-1. 상단 타이틀 영역 ===
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(dayNumber + "일차 결산", JLabel.CENTER);
        titleLabel.setFont(FONT_TITLE);

        JLabel subLabel = new JLabel("오늘 하루도 수고하셨습니다!", JLabel.CENTER);
        subLabel.setFont(FONT_SUB);
        subLabel.setForeground(COLOR_SUBTEXT);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subLabel, BorderLayout.SOUTH);

        cardPanel.add(titlePanel, BorderLayout.NORTH);

        // === 2-2. 중앙 내용 (그리드) ===
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        // 방문 손님 수
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel customerLabel = new JLabel("방문 손님 수:");
        customerLabel.setFont(FONT_LABEL);
        reportPanel.add(customerLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel customerValue = new JLabel(String.format("%,d 명", customerCount));
        customerValue.setFont(FONT_VALUE);
        reportPanel.add(customerValue, gbc);

        // 금일 매출
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel revenueLabel = new JLabel("금일 매출:");
        revenueLabel.setFont(FONT_LABEL);
        reportPanel.add(revenueLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel revenueValue = new JLabel(String.format("%,d 원", revenue));
        revenueValue.setFont(FONT_VALUE);
        reportPanel.add(revenueValue, gbc);

        // 누적 매출
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel totalLabel = new JLabel("누적 매출:");
        totalLabel.setFont(FONT_LABEL);
        totalLabel.setForeground(COLOR_SUBTEXT);
        reportPanel.add(totalLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel totalValue = new JLabel(String.format("%,d 원", totalRevenue));
        totalValue.setFont(FONT_VALUE);
        totalValue.setForeground(COLOR_SUBTEXT);
        reportPanel.add(totalValue, gbc);

        // 구분선
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        reportPanel.add(new JSeparator(), gbc);

        // 순수익
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel netProfitLabel = new JLabel("순수익:");
        netProfitLabel.setFont(FONT_PROFIT);
        reportPanel.add(netProfitLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel netProfitValue = new JLabel(String.format("%,d 원", netProfit));
        netProfitValue.setFont(FONT_PROFIT);
        netProfitValue.setForeground(netProfit >= 0 ? COLOR_PROFIT_GREEN : Color.RED.darker());
        reportPanel.add(netProfitValue, gbc);

        cardPanel.add(reportPanel, BorderLayout.CENTER);

        // === 2-3. 하단 버튼 영역 ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton confirmButton = new JButton("다음 날로 진행");
        confirmButton.setFont(FONT_BUTTON);
        confirmButton.setPreferredSize(new Dimension(220, 48));
        confirmButton.setFocusPainted(false);
        confirmButton.setForeground(Color.WHITE);

        if (netProfit >= 0) {
            confirmButton.setBackground(COLOR_BUTTON_BG);
        } else {
            // 손해를 본 날이면 살짝 더 경고 느낌
            confirmButton.setBackground(COLOR_BUTTON_BG_R);
        }

        confirmButton.setBorderPainted(false);
        confirmButton.addActionListener(e -> this.dispose());

        buttonPanel.add(confirmButton);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        // === 3. 배경 패널에 카드 중앙 배치 ===
        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.gridx = 0;
        rootGbc.gridy = 0;
        backgroundPanel.add(cardPanel, rootGbc);

        setContentPane(backgroundPanel);

        // === 4. 다이얼로그 기본 설정 ===
        this.setSize(650, 550);
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
