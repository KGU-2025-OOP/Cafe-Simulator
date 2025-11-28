package ui;

import stats.StatsService;
import stats.SalesRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;  

public class StatisticsSearchPanel extends JPanel {

	private final StatsService statsService;
	
    private JPanel contentContainer;
    private JButton backButton;
    private Image backgroundImage;

    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> searchTypeCombo;
    
    private JTable resultTable;
    private DefaultTableModel tableModel;
    
    private JLabel lblDays;
    private JLabel lblTotalRevenue;
    private JLabel lblAvgRevenue;
    private JLabel lblOrderCount;
    
    public StatisticsSearchPanel(StatsService statsService) {
    	this.statsService = statsService;
    	
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

        String[] searchTypes = {"메뉴명", "라운드", "주문번호"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        searchTypeCombo.setPreferredSize(new Dimension(110, 40));

        JPanel comboWrapper = new JPanel(new BorderLayout());
        comboWrapper.setOpaque(false);
        comboWrapper.add(searchTypeCombo, BorderLayout.CENTER);
        
        searchField = new JTextField();
        searchField.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        searchField.setBorder(new EmptyBorder(0, 10, 0, 10));

        searchButton = new JButton("🔍");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.setContentAreaFilled(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchBoxPanel.add(comboWrapper, BorderLayout.WEST);
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

        lblDays = new JLabel();
        lblTotalRevenue = new JLabel();
        lblAvgRevenue = new JLabel();
        lblOrderCount = new JLabel();

        lblDays.setFont(summaryFont);
        lblTotalRevenue.setFont(summaryFont);
        lblAvgRevenue.setFont(summaryFont);
        lblOrderCount.setFont(summaryFont);

        summaryPanel.add(lblDays);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lblTotalRevenue);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lblAvgRevenue);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lblOrderCount);
        
        centerPanel.add(summaryPanel, BorderLayout.WEST);

        String[] columnNames = {"라운드", "메뉴", "금액", "옵션"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // 조회 전용
            }
        };
        
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
        
        initActions();     // 버튼/엔터 검색 연결
        fillSummary();     // 좌측 요약 채우기
        loadAllSales();
    }    

    private void initActions() {
        searchField.addActionListener(e -> doSearch());
        searchButton.addActionListener(e -> doSearch());
    }
    
    private void handleSearchAction(ActionEvent e) {
        doSearch();
    }
    
    private void doSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "검색어를 입력해주세요.",
                    "알림",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String type = (String) searchTypeCombo.getSelectedItem();
        java.util.List<SalesRecord> result = new java.util.ArrayList<>();

        try {
            if ("메뉴명".equals(type)) {
                // 부분 일치 검색
                result = statsService.findSalesByMenuName(keyword);

            } else if ("라운드".equals(type)) {
                int round = Integer.parseInt(keyword);
                result = statsService.findSalesByRound(round);

            } else if ("주문번호".equals(type)) {
                result = statsService.findSalesByOrderId(keyword);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "라운드는 숫자로 입력해주세요.",
                    "입력 오류",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "검색 결과가 없습니다.",
                    "결과 없음",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        updateTableWithSales(result);
    }
    
    private void loadAllSales() {
        List<SalesRecord> all = statsService.getAllSalesRecords();
        updateTableWithSales(all);
    }
    
    private void updateTableWithSales(List<SalesRecord> list) {
        tableModel.setRowCount(0);

        for (SalesRecord r : list) {
            String optionsText = String.join(", ", r.getOptions());
            tableModel.addRow(new Object[]{
                    r.getRound(),      // 라운드
                    r.getMenuName(),   // 메뉴
                    r.getPrice(),      // 금액
                    optionsText        // 옵션
            });
        }
    }
    
    private void fillSummary() {
        int dayCount = statsService.getAllDailyRevenues().size();
        int totalRevenue = statsService.getTotalRevenueFromSales();
        double avgPerDay = dayCount == 0 ? 0.0 : (double) totalRevenue / dayCount;
        int orderCount = statsService.getTotalOrderCount();

        lblDays.setText("운영한 일수: " + dayCount + "일");
        lblTotalRevenue.setText("총 수익금: " + totalRevenue + "원");
        lblAvgRevenue.setText(String.format("하루당 평균 수익금: %.1f원", avgPerDay));
        lblOrderCount.setText("누적 방문객 수: " + orderCount + "명");
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