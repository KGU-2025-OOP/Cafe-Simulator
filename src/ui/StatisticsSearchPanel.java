package ui;

import stats.StatsService;
import stats.MenuStatRow;
import stats.OptionStatRow;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Rectangle;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;

public class StatisticsSearchPanel extends JPanel {

    private final StatsService statsService;
    
    private JPanel contentContainer;
    private JButton backButton;
    private Image backgroundImage;

    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JButton searchButton;

    private JTable resultTable;
    private DefaultTableModel tableModel;

    private JLabel lblDays;
    private JLabel lblTotalRevenue;
    private JLabel lblAvgRevenue;
    private JLabel lblOrderCount;

    private static final String TYPE_DRINK = "음료";
    private static final String TYPE_OPTION = "옵션";

    private static final String[] DRINK_COLUMNS = {
            "라운드", "음료명", "판매개수", "판매총액"
    };

    private static final String[] OPTION_COLUMNS = {
            "라운드", "옵션명", "판매개수", "판매총액"
    };

    private TableRowSorter<DefaultTableModel> sorter;
    private int lastSortedColumn = -1;     
    private boolean lastSortAscending = false;

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

        JLabel titleLabel = new JLabel("통계 검색     ") {
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

                g2.setColor(new Color(160, 110, 80));
                g2.drawString(text, x, y);
            }
        };
        
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setPreferredSize(new Dimension(250, 60));
        titlePanel.add(titleLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 30, 0, 30));
        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel outerContent = new JPanel(new BorderLayout());
        outerContent.setOpaque(false);
        outerContent.setBorder(new EmptyBorder(10, 30, 30, 30));

        contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(true);
        contentContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentContainer.setBackground(new Color(255, 255, 255, 180));

        outerContent.add(contentContainer, BorderLayout.CENTER);
        add(outerContent, BorderLayout.CENTER);

        JPanel searchOuter = new JPanel(new GridBagLayout());
        searchOuter.setOpaque(false);

        JPanel searchBoxPanel = new JPanel(new BorderLayout());
        searchBoxPanel.setPreferredSize(new Dimension(550, 60));
        searchBoxPanel.setBorder(BorderFactory.createLineBorder(new Color(160, 110, 80), 3));
        searchBoxPanel.setBackground(Color.WHITE);

        searchTypeCombo = new JComboBox<>(new String[]{TYPE_DRINK, TYPE_OPTION});
        searchTypeCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        searchTypeCombo.setPreferredSize(new Dimension(100, 40));
        searchTypeCombo.setBackground(new Color(160, 110, 80));

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
        
        searchButton.setOpaque(true);
        searchButton.setContentAreaFilled(true);  
        searchButton.setBackground(new Color(160, 110, 80)); 
        searchButton.setForeground(Color.WHITE); 
        
        searchBoxPanel.add(comboWrapper, BorderLayout.WEST);
        searchBoxPanel.add(searchField, BorderLayout.CENTER);
        searchBoxPanel.add(searchButton, BorderLayout.EAST);

        searchOuter.add(searchBoxPanel);
        contentContainer.add(searchOuter, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentContainer.add(centerPanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 6, w - 4, h - 4, 25, 25);

                g2.setColor(new Color(255, 250, 240, 230));
                g2.fillRoundRect(0, 0, w - 8, h - 8, 25, 25);

                g2.setColor(new Color(180, 150, 120, 200));
                g2.drawRoundRect(0, 0, w - 8, h - 8, 25, 25);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        summaryPanel.setOpaque(false);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setPreferredSize(new Dimension(260, 210));
        summaryPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel summaryTitle = new JLabel("요약 통계");
        summaryTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        summaryTitle.setForeground(new Color(70, 45, 30));
        summaryTitle.setAlignmentX(LEFT_ALIGNMENT);

        Font summaryFont = new Font("Malgun Gothic", Font.BOLD, 15);  
        Color summaryColor = new Color(90, 65, 45); 
        int verticalGap = 6;                        

        lblDays = new JLabel();
        lblTotalRevenue = new JLabel();
        lblAvgRevenue = new JLabel();
        lblOrderCount = new JLabel();

        JLabel[] labels = {lblDays, lblTotalRevenue, lblAvgRevenue, lblOrderCount};

        for (JLabel lbl : labels) {
            lbl.setFont(summaryFont);
            lbl.setForeground(summaryColor);
            lbl.setAlignmentX(LEFT_ALIGNMENT);

            lbl.setBorder(new EmptyBorder(0, 2, 0, 0));
        }

        summaryPanel.add(summaryTitle);
        summaryPanel.add(Box.createVerticalStrut(12)); 

        summaryPanel.add(lblDays);
        summaryPanel.add(Box.createVerticalStrut(verticalGap));

        summaryPanel.add(lblTotalRevenue);
        summaryPanel.add(Box.createVerticalStrut(verticalGap));

        summaryPanel.add(lblAvgRevenue);
        summaryPanel.add(Box.createVerticalStrut(verticalGap));

        summaryPanel.add(lblOrderCount);

        centerPanel.add(summaryPanel, BorderLayout.WEST);


        tableModel = new DefaultTableModel(DRINK_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel) {
            @Override
            public java.awt.Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int column) {

                java.awt.Component c = super.prepareRenderer(renderer, row, column);
                
                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                }

                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(true);
                }

                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 250, 240));
                    } else {
                        c.setBackground(new Color(250, 245, 235));
                    }
                } else {
                    c.setBackground(new Color(230, 210, 180));
                }

                return c;
            }
        };

        resultTable.setFillsViewportHeight(true);
        resultTable.setRowHeight(26);
        resultTable.setShowGrid(false);
   
        resultTable.setIntercellSpacing(new Dimension(0, 0));
        resultTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
        resultTable.setForeground(new Color(60, 50, 40));
        
        JTableHeader header = resultTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        header.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                lbl.setHorizontalAlignment(CENTER);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(90, 60, 40, 240));
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return lbl;
            }
        });

        sorter = new TableRowSorter<>(tableModel);
        resultTable.setRowSorter(sorter);

        addHeaderSortListener();

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setUI(new CafeScrollBarUI());

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        searchTypeCombo.addActionListener(e -> {
            updateTableColumns();
            doSearch();
        });

        searchField.addActionListener(e -> doSearch());
        searchButton.addActionListener(e -> doSearch());

        fillSummary();
        
        doSearch();
    }

    private void addHeaderSortListener() {
        resultTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = resultTable.columnAtPoint(e.getPoint());
                handleSort(col);
            }
        });
    }
    
    private void updateHeaderSortIndicators() {
        if (resultTable == null) return;

        TableColumnModel columnModel = resultTable.getColumnModel();
        String selected = (String) searchTypeCombo.getSelectedItem();
        String[] baseColumns = TYPE_OPTION.equals(selected) ? OPTION_COLUMNS : DRINK_COLUMNS;

        for (int i = 0; i < baseColumns.length; i++) {
            String name = baseColumns[i];

            if (i == lastSortedColumn) {
                if (lastSortAscending) {
                    name += " ▲";
                } else {
                    name += " ▼";
                }
            }

            columnModel.getColumn(i).setHeaderValue(name);
        }

        resultTable.getTableHeader().repaint();
    }

    private void handleSort(int col) {
        if (sorter == null) return;

        if (lastSortedColumn != col) {
            lastSortedColumn = col;
            lastSortAscending = false;
        } else {
            lastSortAscending = !lastSortAscending;
        }

        SortOrder order = lastSortAscending ? SortOrder.ASCENDING : SortOrder.DESCENDING;

        sorter.setSortKeys(java.util.List.of(
                new RowSorter.SortKey(col, order)
        ));
        
        updateHeaderSortIndicators();
    }

    private void updateTableColumns() {
        String selected = (String) searchTypeCombo.getSelectedItem();
        String[] columns;

        if (TYPE_OPTION.equals(selected)) {
            columns = OPTION_COLUMNS;
        } else {
            columns = DRINK_COLUMNS;
        }

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable.setModel(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        resultTable.setRowSorter(sorter);

        lastSortedColumn = -1;
        lastSortAscending = false;

        resultTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        resultTable.getTableHeader().setReorderingAllowed(false);
        
       
        updateHeaderSortIndicators();
    }

    private void doSearch() {
        if (statsService == null) {
            return;
        }

        String keyword = searchField.getText().trim();
        String selected = (String) searchTypeCombo.getSelectedItem();

        try {
            boolean isRoundNumber = !keyword.isEmpty() && keyword.matches("\\d+");

            if (TYPE_OPTION.equals(selected)) {
                List<OptionStatRow> rows;

                if (keyword.isEmpty()) {
                    rows = statsService.getAllOptionStatsByRoundAndOption();
                } else if (isRoundNumber) {
                    int round = Integer.parseInt(keyword);
                    rows = statsService.searchOptionStatsByRound(round);
                } else {
                    rows = statsService.searchOptionStatsByOptionKeyword(keyword);
                }

                showOptionStats(rows);

            } else {
                List<MenuStatRow> rows;

                if (keyword.isEmpty()) {
                    rows = statsService.getAllMenuStatsByRoundAndMenu();
                } else if (isRoundNumber) {
                    int round = Integer.parseInt(keyword);
                    rows = statsService.searchMenuStatsByRound(round);
                } else {
                    rows = statsService.searchMenuStatsByMenuKeyword(keyword);
                }

                showMenuStats(rows);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "통계 데이터를 불러오는 중 오류가 발생했습니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    
    private void showMenuStats(List<MenuStatRow> rows) {
        tableModel.setRowCount(0);

        if (rows != null) {
            for (MenuStatRow r : rows) {
                tableModel.addRow(new Object[]{
                        r.getRound(),
                        r.getMenuName(),
                        r.getQuantity(),
                        r.getTotalRevenue()
                });
            }
        }

        lastSortedColumn = -1;
        lastSortAscending = false;
        if (sorter != null) {
            sorter.setSortKeys(null);
        }
    }
    
    private void showOptionStats(List<OptionStatRow> rows) {
        tableModel.setRowCount(0);

        if (rows != null) {
            for (OptionStatRow r : rows) {
                tableModel.addRow(new Object[]{
                        r.getRound(),
                        r.getOptionName(),
                        r.getQuantity(),
                        r.getTotalRevenue()
                });
            }
        }

        lastSortedColumn = -1;
        lastSortAscending = false;
        if (sorter != null) {
            sorter.setSortKeys(null);
        }
    }

    public void refreshData() {
        fillSummary();
        doSearch();
    }

    private void fillSummary() {
        if (statsService == null) {
            lblDays.setText("운영한 일수: -일");
            lblTotalRevenue.setText("총 수익금: -원");
            lblAvgRevenue.setText("하루당 평균 수익금: -원");
            lblOrderCount.setText("누적 방문객 수: -명");
            return;
        }

        try {
            int dayCount = statsService.getAllDailyRevenues().size();
            int totalRevenue = statsService.getTotalRevenueFromSales();
            double avgPerDay = (dayCount == 0) ? 0.0 : (double) totalRevenue / dayCount;
            int customerCount = statsService.getUniqueCustomerCount();

            lblDays.setText("운영한 일수: " + dayCount + "일");
            lblTotalRevenue.setText("총 수익금: " + totalRevenue + "원");
            lblAvgRevenue.setText(String.format("하루당 평균 수익금: %.1f원", avgPerDay));
            lblOrderCount.setText("누적 방문객 수: " + customerCount + "명");
        } catch (Exception e) {
            lblDays.setText("운영한 일수: -일");
            lblTotalRevenue.setText("총 수익금: -원");
            lblAvgRevenue.setText("하루당 평균 수익금: -원");
            lblOrderCount.setText("누적 방문객 수: -명");
        }
    }
    
    private void resetState() {
        if (searchTypeCombo != null) {
            searchTypeCombo.setSelectedItem(TYPE_DRINK);
        }
        if (searchField != null) {
            searchField.setText("");
        }

        lastSortedColumn = -1;
        lastSortAscending = false;

        updateTableColumns();
        
        fillSummary();
        doSearch();
    }
    
    private static class CafeScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(160, 110, 80));
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4,
                    10, 10);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            jbutton.setContentAreaFilled(false);
            jbutton.setBorderPainted(false);
            jbutton.setFocusPainted(false);
            return jbutton;
        }
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
    
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);

        if (aFlag) {
            resetState();
        }
    }
}
