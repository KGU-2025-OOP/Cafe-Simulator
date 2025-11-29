package ui;

import stats.StatsService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import java.awt.*;

/**
 * 통계 검색 화면
 * - 왼쪽: 전체 요약 통계(운영일수, 총 수익, 평균 수익, 방문객 수)
 * - 오른쪽 상단: 검색 타입(음료/옵션) + 검색어 + 검색 버튼
 * - 오른쪽 메인: 선택 타입에 따른 테이블
 *      음료  → 라운드 | 메뉴   | 판매개수 | 판매총액
 *      옵션  → 라운드 | 옵션명 | 판매개수 | 판매총액
 * - 테이블 헤더 클릭 시 정렬:
 *      처음 상태: 입력 순
 *      같은 컬럼 1번 클릭 → 내림차순
 *      같은 컬럼 다시 클릭 → 오름차순
 *      이후 반복(내림 ↔ 오름 토글)
 */
public class StatisticsSearchPanel extends JPanel {

    // ====== 서비스 ======
    private final StatsService statsService;  // 왼쪽 요약 통계용 (검색/집계는 나중에 연결)

    // ====== UI 컴포넌트 ======
    private JPanel contentContainer;
    private JButton backButton;
    private Image backgroundImage;

    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JButton searchButton;

    private JTable resultTable;
    private DefaultTableModel tableModel;

    // 왼쪽 요약 라벨
    private JLabel lblDays;
    private JLabel lblTotalRevenue;
    private JLabel lblAvgRevenue;
    private JLabel lblOrderCount;

    // 검색 타입
    private static final String TYPE_DRINK = "음료";
    private static final String TYPE_OPTION = "옵션";

    // 테이블 컬럼 정의
    // 메뉴: 라운드 메뉴 판매개수 판매총액
    private static final String[] DRINK_COLUMNS = {
            "라운드", "메뉴", "판매개수", "판매총액"
    };

    // 옵션: 라운드 옵션명 판매개수 판매총액
    private static final String[] OPTION_COLUMNS = {
            "라운드", "옵션명", "판매개수", "판매총액"
    };

    // ====== 정렬 관련 ======
    private TableRowSorter<DefaultTableModel> sorter;
    private int lastSortedColumn = -1;      // 마지막으로 정렬한 컬럼 인덱스
    private boolean lastSortAscending = false; // 마지막 정렬이 오름차순인지 여부

    public StatisticsSearchPanel(StatsService statsService) {
        this.statsService = statsService;

        backgroundImage = ImageManager.getImage(ImageManager.IMG_MENU_BG);

        setLayout(new BorderLayout());
        setPreferredSize(ScreenConfig.FRAME_SIZE);
        setOpaque(false);

        // =========================
        // 상단: 뒤로가기 + 제목
        // =========================
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

        JLabel titleLabel = new JLabel("통계 검색") {
            @Override
            protected void paintComponent(Graphics g) {
                // 제목에 약간의 그림자 효과
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
        topPanel.setBorder(new EmptyBorder(20, 30, 0, 30));
        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // =========================
        // 본문 컨테이너
        // =========================
        JPanel outerContent = new JPanel(new BorderLayout());
        outerContent.setOpaque(false);
        outerContent.setBorder(new EmptyBorder(10, 30, 30, 30));

        contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(true);
        contentContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentContainer.setBackground(new Color(255, 255, 255, 180)); // 반투명 흰색

        outerContent.add(contentContainer, BorderLayout.CENTER);
        add(outerContent, BorderLayout.CENTER);

        // =========================
        // 검색 영역 (타입 + 키워드 + 버튼)
        // =========================
        JPanel searchOuter = new JPanel(new GridBagLayout());
        searchOuter.setOpaque(false);

        JPanel searchBoxPanel = new JPanel(new BorderLayout());
        searchBoxPanel.setPreferredSize(new Dimension(550, 60));
        searchBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        searchBoxPanel.setBackground(Color.WHITE);

        // 타입 선택 콤보박스: 음료 / 옵션
        searchTypeCombo = new JComboBox<>(new String[]{TYPE_DRINK, TYPE_OPTION});
        searchTypeCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        searchTypeCombo.setPreferredSize(new Dimension(100, 40));

        JPanel comboWrapper = new JPanel(new BorderLayout());
        comboWrapper.setOpaque(false);
        comboWrapper.add(searchTypeCombo, BorderLayout.CENTER);

        // 검색어 입력 필드 (나중에 데이터 검색에 사용할 예정)
        searchField = new JTextField();
        searchField.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        searchField.setBorder(new EmptyBorder(0, 10, 0, 10));

        // 검색 버튼 (동작은 나중에 구현)
        searchButton = new JButton("🔍");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.setContentAreaFilled(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 검색 박스 레이아웃: [타입콤보] [검색어] [버튼]
        searchBoxPanel.add(comboWrapper, BorderLayout.WEST);
        searchBoxPanel.add(searchField, BorderLayout.CENTER);
        searchBoxPanel.add(searchButton, BorderLayout.EAST);

        searchOuter.add(searchBoxPanel);
        contentContainer.add(searchOuter, BorderLayout.NORTH);

        // =========================
        // 가운데: 왼쪽 요약 + 오른쪽 테이블
        // =========================
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentContainer.add(centerPanel, BorderLayout.CENTER);

        // --- 왼쪽 요약 패널 ---
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

        // --- 오른쪽 결과 테이블 ---
        // 기본은 "음료" 기준 컬럼으로 시작
        tableModel = new DefaultTableModel(DRINK_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 조회 전용
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setFillsViewportHeight(true);
        resultTable.setRowHeight(24);
        resultTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        resultTable.getTableHeader().setReorderingAllowed(false);

        // sorter 설정
        sorter = new TableRowSorter<>(tableModel);
        resultTable.setRowSorter(sorter);

        // 헤더 클릭 시 정렬 핸들러 추가
        addHeaderSortListener();

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // =========================
        // 이벤트 설정
        // =========================

        // 타입 변경 시 테이블 컬럼 헤더 변경
        searchTypeCombo.addActionListener(e -> updateTableColumns());

        // 검색 버튼/엔터 → 나중에 StatsService 검색/집계 연결 예정
        searchField.addActionListener(e -> doSearchStub());
        searchButton.addActionListener(e -> doSearchStub());

        // 왼쪽 요약 통계 채우기
        fillSummary();
    }

    /**
     * 테이블 헤더 클릭 시 정렬 동작 설정.
     * - 처음 클릭: 해당 컬럼 내림차순
     * - 다시 클릭: 오름차순
     * - 이후 반복 (내림 ↔ 오름)
     */
    private void addHeaderSortListener() {
        resultTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = resultTable.columnAtPoint(e.getPoint());
                handleSort(col);
            }
        });
    }

    private void handleSort(int col) {
        if (sorter == null) return;

        if (lastSortedColumn != col) {
            // 다른 컬럼을 새로 클릭 → 이 컬럼 기준 내림차순으로 시작
            lastSortedColumn = col;
            lastSortAscending = false; // 내림차순
        } else {
            // 같은 컬럼 다시 클릭 → 방향 토글
            lastSortAscending = !lastSortAscending;
        }

        SortOrder order = lastSortAscending ? SortOrder.ASCENDING : SortOrder.DESCENDING;

        sorter.setSortKeys(java.util.List.of(
                new RowSorter.SortKey(col, order)
        ));
    }

    /**
     * 검색 타입(음료/옵션) 변경 시 테이블 컬럼 헤더만 변경.
     * 실제 데이터는 나중에 StatsService 집계 결과로 채울 예정.
     */
    private void updateTableColumns() {
        String selected = (String) searchTypeCombo.getSelectedItem();
        String[] columns;

        if (TYPE_OPTION.equals(selected)) {
            columns = OPTION_COLUMNS;
        } else {
            // 기본: 음료
            columns = DRINK_COLUMNS;
        }

        // 기존 모델 교체 (데이터는 초기화)
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable.setModel(tableModel);

        // sorter 다시 설정
        sorter = new TableRowSorter<>(tableModel);
        resultTable.setRowSorter(sorter);

        // 컬럼 바뀌었으니 정렬 상태 초기화
        lastSortedColumn = -1;
        lastSortAscending = false;

        resultTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        resultTable.getTableHeader().setReorderingAllowed(false);
        // 헤더 클릭 리스너는 한 번만 추가했으므로 그대로 동작함
    }

    /**
     * 나중에 StatsService 집계 메서드 연결할 자리.
     * 지금은 빈 껍데기.
     */
    private void doSearchStub() {
        // TODO:
        //  - searchTypeCombo (음료 / 옵션)
        //  - searchField.getText() (검색어)
        // 기준으로 StatsService 집계 메서드 호출
        //  - 결과를 Object[][] 형태로 만들어 tableModel.addRow(...) 로 채우기
        //
        // ex)
        // Object[][] rows = ...;
        // updateTableData(rows);
    }

    /**
     * 나중에 실제 데이터 채울 때 사용할 헬퍼 (현재는 호출 안 함).
     */
    @SuppressWarnings("unused")
    private void updateTableData(Object[][] rows) {
        tableModel.setRowCount(0);
        if (rows == null) return;

        for (Object[] r : rows) {
            tableModel.addRow(r);
        }

        // 새로운 데이터가 들어왔으니 정렬 상태만 초기화
        lastSortedColumn = -1;
        lastSortAscending = false;
        if (sorter != null) {
            sorter.setSortKeys(null);
        }
    }

    /**
     * 왼쪽 요약 정보 채우기.
     * StatsService에서 제공하는 통계 메서드를 사용.
     */
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
            int orderCount = statsService.getTotalOrderCount();

            lblDays.setText("운영한 일수: " + dayCount + "일");
            lblTotalRevenue.setText("총 수익금: " + totalRevenue + "원");
            lblAvgRevenue.setText(String.format("하루당 평균 수익금: %.1f원", avgPerDay));
            lblOrderCount.setText("누적 방문객 수: " + orderCount + "명");
        } catch (Exception e) {
            lblDays.setText("운영한 일수: -일");
            lblTotalRevenue.setText("총 수익금: -원");
            lblAvgRevenue.setText("하루당 평균 수익금: -원");
            lblOrderCount.setText("누적 방문객 수: -명");
        }
    }

    // 배경 이미지 렌더링
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
