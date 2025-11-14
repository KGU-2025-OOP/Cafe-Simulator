package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class CalendarDialog extends JDialog
{
    private static final LocalDate START_DATE = LocalDate.of(2025, 11, 13);

    private Map<String, Integer> dailySalesHistory;
    private boolean shouldReopenPause = false;

    private JLabel selectedDayLabel;
    private JLabel salesAmountLabel;

    public CalendarDialog(JFrame parent, LocalDate currentSystemDate, Map<String, Integer> dailySalesHistory)
    {
        super(parent, "달력", true);
        this.dailySalesHistory = dailySalesHistory;

        JPanel base = new JPanel(new BorderLayout());
        base.setBackground(new Color(255, 249, 235));
        base.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JButton backBtn = this.createSoftButton("←", 30);
        backBtn.addActionListener(e ->
        {
            this.shouldReopenPause = true;
            this.dispose();
        });

        JPanel backWrap = new JPanel(new BorderLayout());
        backWrap.setOpaque(false);
        backWrap.add(backBtn, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("일차별 매출 현황", JLabel.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setForeground(new Color(80, 60, 50));

        topPanel.add(backWrap, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        base.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(40, 0));
        centerPanel.setOpaque(false);

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setOpaque(false);
        dataPanel.setPreferredSize(new Dimension(420, 500));

        JPanel dataCard = new JPanel(new GridBagLayout());
        dataCard.setBackground(new Color(255, 243, 219));
        dataCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 210, 180), 2, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        this.selectedDayLabel = new JLabel("데이터 출력 화면");
        this.selectedDayLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
        this.selectedDayLabel.setForeground(new Color(80, 60, 50));

        this.salesAmountLabel = new JLabel("--- 원");
        this.salesAmountLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 42));
        this.salesAmountLabel.setForeground(new Color(60, 40, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        dataCard.add(this.selectedDayLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        dataCard.add(this.salesAmountLabel, gbc);

        dataPanel.add(dataCard);
        centerPanel.add(dataPanel, BorderLayout.WEST);

        JPanel calendarWrapper = new JPanel(new GridBagLayout());
        calendarWrapper.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(4, 4, 20, 20));
        grid.setOpaque(false);

        long currentDayNumber = ChronoUnit.DAYS.between(START_DATE, currentSystemDate) + 1;

        for (int i = 1; i <= 16; i++)
        {
            JButton btn = this.createDayButton(i + "일");

            if (i == currentDayNumber)
            {
                btn.setBackground(new Color(255, 227, 160));
                btn.setBorder(new LineBorder(new Color(225, 175, 80), 3, true));
            }

            LocalDate d = START_DATE.plusDays(i - 1);
            String key = d.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int dayNum = i;

            btn.addActionListener(e ->
            {
                int sales = this.dailySalesHistory.getOrDefault(key, Integer.valueOf(0));
                this.updateSalesInfo(dayNum, sales);
            });

            grid.add(btn);
        }

        calendarWrapper.add(grid);
        centerPanel.add(calendarWrapper, BorderLayout.CENTER);

        base.add(centerPanel, BorderLayout.CENTER);

        this.add(base);

        this.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        int w = gd.getDisplayMode().getWidth();
        int h = gd.getDisplayMode().getHeight();

        this.setSize(w, h);
        this.setLocationRelativeTo(null);
    }

    private JButton createSoftButton(String text, int size)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, size));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 239, 210));
        btn.setForeground(new Color(90, 70, 60));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 205, 175), 2, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        return btn;
    }

    private JButton createDayButton(String text)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 243, 220));
        btn.setForeground(new Color(70, 55, 45));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 200, 170), 2, true),
                new EmptyBorder(20, 10, 20, 10)
        ));
        return btn;
    }

    private void updateSalesInfo(int day, int sales)
    {
        this.selectedDayLabel.setText(day + "일차 매출");
        this.salesAmountLabel.setText(String.format("%,d원", Integer.valueOf(sales)));
    }

    public boolean shouldReopenPause()
    {
        return this.shouldReopenPause;
    }
}