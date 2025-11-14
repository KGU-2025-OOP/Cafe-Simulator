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

public class DayEndDialog extends JDialog
{
    private static final Font FONT_TITLE = new Font("Malgun Gothic", Font.BOLD, 28);
    private static final Font FONT_LABEL = new Font("Malgun Gothic", Font.PLAIN, 20);
    private static final Font FONT_VALUE = new Font("Malgun Gothic", Font.BOLD, 20);
    private static final Font FONT_PROFIT = new Font("Malgun Gothic", Font.BOLD, 28);

    public DayEndDialog(JFrame parent, int dayNumber, int customerCount, int revenue, int orderCost)
    {
        super(parent, dayNumber + "일차 결산", true);

        int netProfit = revenue - orderCost;

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

        // --- 방문 손님 수 ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel customerLabel = new JLabel("방문 손님 수:");
        customerLabel.setFont(FONT_LABEL);
        reportPanel.add(customerLabel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        // [수정] Integer.valueOf() 추가
        JLabel customerValue = new JLabel(String.format("%,d 명", Integer.valueOf(customerCount)));
        customerValue.setFont(FONT_VALUE);
        reportPanel.add(customerValue, gbc);

        // --- 총 수익 ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JLabel revenueLabel = new JLabel("총 수익 (매출):");
        revenueLabel.setFont(FONT_LABEL);
        reportPanel.add(revenueLabel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        // [수정] Integer.valueOf() 추가
        JLabel revenueValue = new JLabel(String.format("%,d 원", Integer.valueOf(revenue)));
        revenueValue.setFont(FONT_VALUE);
        reportPanel.add(revenueValue, gbc);

        // --- 발주 비용 ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        JLabel orderCostLabel = new JLabel("발주 비용 (지출):");
        orderCostLabel.setFont(FONT_LABEL);
        reportPanel.add(orderCostLabel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        // [수정] Integer.valueOf() 추가
        JLabel orderCostValue = new JLabel(String.format("%,d 원", Integer.valueOf(orderCost)));
        orderCostValue.setFont(FONT_VALUE);
        orderCostValue.setForeground(Color.RED.darker());
        reportPanel.add(orderCostValue, gbc);

        // --- 구분선 ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        reportPanel.add(new JSeparator(), gbc);

        // --- 순수익 ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel netProfitLabel = new JLabel("순수익:");
        netProfitLabel.setFont(FONT_PROFIT);
        reportPanel.add(netProfitLabel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        // [수정] Integer.valueOf() 추가
        JLabel netProfitValue = new JLabel(String.format("%,d 원", Integer.valueOf(netProfit)));
        netProfitValue.setFont(FONT_PROFIT);
        netProfitValue.setForeground(netProfit >= 0 ? new Color(0, 120, 0) : Color.RED.darker());
        reportPanel.add(netProfitValue, gbc);

        mainPanel.add(reportPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton confirmButton = new JButton("다음 날로 (확인)");
        confirmButton.setFont(FONT_LABEL);
        confirmButton.setPreferredSize(new Dimension(200, 50));
        confirmButton.addActionListener(e -> dispose());
        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}