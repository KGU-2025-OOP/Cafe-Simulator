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

public class DaySummaryDialog extends JDialog {

    private static final Font FONT_TITLE = new Font("Malgun Gothic", Font.BOLD, 28);
    private static final Font FONT_LABEL = new Font("Malgun Gothic", Font.PLAIN, 20);
    private static final Font FONT_VALUE = new Font("Malgun Gothic", Font.BOLD, 20);
    private static final Font FONT_PROFIT = new Font("Malgun Gothic", Font.BOLD, 28);

    private static final Color COLOR_PROFIT_GREEN = new Color(0, 120, 0);

    public DaySummaryDialog(JFrame parent, int dayNumber, int customerCount, int revenue, long totalRevenue) {
        super(parent, dayNumber + "일차 결산", true);

        int netProfit = revenue;

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel(dayNumber + "일차 결산", JLabel.CENTER);
        titleLabel.setFont(FONT_TITLE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

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

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel totalLabel = new JLabel("누적 매출:");
        totalLabel.setFont(FONT_LABEL);
        totalLabel.setForeground(new Color(100, 100, 100));
        reportPanel.add(totalLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel totalValue = new JLabel(String.format("%,d 원", totalRevenue));
        totalValue.setFont(FONT_VALUE);
        totalValue.setForeground(new Color(100, 100, 100));
        reportPanel.add(totalValue, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        reportPanel.add(new JSeparator(), gbc);

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

        mainPanel.add(reportPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton confirmButton = new JButton("다음 날로 (확인)");
        confirmButton.setFont(FONT_LABEL);
        confirmButton.setPreferredSize(new Dimension(200, 50));
        confirmButton.addActionListener(e -> this.dispose());

        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}