package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Map;

public class CalendarViewDialog extends JDialog {
	
	private boolean reopenPause = false;

    private JLabel dayLabel;
    private JLabel amountLabel;

    private int currentDay;
    private Map<Integer, Integer> dailySales;

    public CalendarViewDialog(JFrame parent, int currentDay, Map<Integer, Integer> dailySales) {
        super(parent, true);
        this.currentDay = currentDay;
        this.dailySales = dailySales;

        setUndecorated(true);
        setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenW = gd.getDisplayMode().getWidth();
        int screenH = gd.getDisplayMode().getHeight();
        setSize(screenW, screenH);
        setLocationRelativeTo(null);

        add(createTitleBar(), BorderLayout.NORTH);
        add(createCenter(screenW, screenH), BorderLayout.CENTER);
    }

    private JPanel createTitleBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(30, 40, 20, 40));

        JButton back = new JButton("←");
        back.setFont(new Font("Malgun Gothic", Font.BOLD, 26));
        back.setBackground(new Color(255, 233, 210));
        back.setBorder(new LineBorder(new Color(200,160,140), 2, true));
        back.addActionListener(e -> {
            reopenPause = true;
            dispose();
        });

        JLabel title = new JLabel("일차별 매출 현황", SwingConstants.CENTER);
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 40));

        top.add(back, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        
        return top;
    }

    private JPanel createCenter(int screenW, int screenH) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setPreferredSize(new Dimension((int)(screenW * 0.65), (int)(screenH * 0.55)));

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel left = createSalesBox();
        JPanel right = createCalendarGrid();

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 70);
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(left, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 70, 0, 0);
        content.add(right, gbc);

        GridBagConstraints og = new GridBagConstraints();
        og.anchor = GridBagConstraints.CENTER;
        outer.add(content, og);

        return outer;
    }

    private JPanel createSalesBox() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 244, 210));
        card.setPreferredSize(new Dimension(520, 360));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(200,160,140), 3, true),
                new EmptyBorder(45, 45, 45, 45)
        ));

        dayLabel = new JLabel("날짜를 선택하세요");
        dayLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 32));

        amountLabel = new JLabel("--- 원");
        amountLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 58));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        card.add(dayLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(25, 0, 0, 0);
        card.add(amountLabel, gbc);

        return card;
    }

    private JPanel createCalendarGrid() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(4, 4, 20, 20));
        grid.setOpaque(false);

        for (int i = 1; i <= 16; i++) {
            JButton btn = createDayButton(i);

            if (i == currentDay) {
                btn.setBackground(new Color(255, 220, 145));
                btn.setBorder(new LineBorder(new Color(220,150,60), 3, true));
            }

            grid.add(btn);
        }

        wrap.add(grid);
        
        return wrap;
    }

    private JButton createDayButton(int day) {
        JButton btn = new JButton(day + "일");
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        btn.setBackground(new Color(255, 236, 210));
        btn.setForeground(new Color(70, 55, 45));
        btn.setFocusPainted(false);

        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(220,200,170), 2, true),
                new EmptyBorder(25, 20, 25, 20)
        ));

        btn.addActionListener(e -> updateSales(day));
        
        return btn;
    }

    private void updateSales(int day) {
        int amount = dailySales.getOrDefault(day, 0);
        
        dayLabel.setText(day + "일차 매출");
        amountLabel.setText(String.format("%,d 원", amount));
    }

    public boolean shouldReopenPause() {
        return reopenPause;
    }
}