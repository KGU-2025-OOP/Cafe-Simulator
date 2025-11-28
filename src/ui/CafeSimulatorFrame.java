package ui;

import stats.StatsService;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Cursor;
import java.awt.Dimension;

public class CafeSimulatorFrame extends JFrame {

    private static final int MIN_CUSTOMERS = 10;
    private static final int MAX_EXTRA_CUSTOMERS = 15;
    private static final int AVG_SPEND_PER_CUSTOMER = 3000;
    private static final int SPEND_VARIANCE = 2000;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image mainBackgroundImage;

    private StartMenuPanel startPanel;
    private NewGameSetupPanel newGamePanel;
    private GameplayPanel gameScreen;
    private GameplayAreaPanel gameSpacePanel;
    private SalesStatisticsPanel salesGraphPanel;
    private StatisticsSearchPanel statisticsSearchPanel;
    private MenuGuidePanel menuGuidePanel;

    private int currentDayNumber;
    private Map<Integer, Integer> dailySalesHistory;
    private List<MenuItem> allMenuItems;

    private long totalAccumulatedRevenue = 0;

    private JPanel bottomBarPanel;
    private JPanel bottomLeftPanel;
    private JPanel bottomRightPanel;

    private JButton exitButton;
    private JButton giveUpButton;
    private JButton menuButton;
    private JToggleButton bgmButton;

    private String currentPanelName = "Start";
    private String previousPanelName = "GameSpaceHub";
    public static final String SAVE_FILE_PATH = "cafename.txt";
    private String cafeName;

    private StatsService statsService;

    public static void mymain(String[] args) {
        File saveFile = new File(SAVE_FILE_PATH);
        final boolean MOCK_SAVE_FILE_EXISTS = saveFile.exists();
        SwingUtilities.invokeLater(() -> {
            CafeSimulatorFrame game = new CafeSimulatorFrame(MOCK_SAVE_FILE_EXISTS);
            game.setVisible(true);
        });
    }

    public CafeSimulatorFrame(boolean hasSaveFile) {
        setTitle("My Cafe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        mainBackgroundImage = ImageManager.getImage(ImageManager.IMG_MAIN_BG);

        setLayout(new BorderLayout());

        if (hasSaveFile) {
            try {
                FileReader fr = new FileReader(CafeSimulatorFrame.SAVE_FILE_PATH);
                BufferedReader br = new BufferedReader(fr);
                String[] saveData = br.readLine().split(" ");
                cafeName = saveData[0];
                currentDayNumber = Integer.parseInt(saveData[1]);
                br.close();
            } catch (FileNotFoundException e) {
                assert(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            currentDayNumber = 1;
        }

        dailySalesHistory = new LinkedHashMap<>();
        totalAccumulatedRevenue = 0;

        initializeMenuItems();
        initStatsService();

        cardLayout = new CardLayout();

        mainPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mainBackgroundImage != null) {
                    g.drawImage(mainBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        mainPanel.setPreferredSize(ScreenConfig.FRAME_SIZE);

        startPanel = new StartMenuPanel(hasSaveFile);
        mainPanel.add(startPanel, "Start");

        // [수정] 기본 생성자 사용 (뒤로가기 버튼 제거됨)
        newGamePanel = new NewGameSetupPanel();
        mainPanel.add(newGamePanel, "Nickname");

        gameScreen = new GameplayPanel();
        mainPanel.add(gameScreen, "Game");
        gameScreen.setDay(currentDayNumber);

        gameSpacePanel = new GameplayAreaPanel();
        mainPanel.add(gameSpacePanel, "GameSpaceHub");

        salesGraphPanel = new SalesStatisticsPanel(dailySalesHistory);
        mainPanel.add(salesGraphPanel, "Graph");

        statisticsSearchPanel = new StatisticsSearchPanel(statsService);
        mainPanel.add(statisticsSearchPanel, "Search");

        menuGuidePanel = new MenuGuidePanel(allMenuItems, this::handleMenuBack);
        mainPanel.add(menuGuidePanel, "MenuGuide");

        addListenersToStartPanel(hasSaveFile);
        addListenersToNewGamePanel();
        addListenersToGameScreen();
        addListenersToGameSpacePanel();

        salesGraphPanel.getBackButton().addActionListener(e -> {
            showPanel("GameSpaceHub");
        });

        statisticsSearchPanel.getBackButton().addActionListener(e -> {
            showPanel("GameSpaceHub");
        });

        add(mainPanel, BorderLayout.CENTER);

        createBottomBar();
        add(bottomBarPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        BgmManager.setOn(true);

        showPanel("Start");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            }
        });
    }

    private void createBottomBar() {
        bottomBarPanel = new JPanel(new BorderLayout());

        Color barColor = new Color(222, 184, 135);
        bottomBarPanel.setBackground(barColor);

        bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeftPanel.setOpaque(false);

        exitButton = createMiniButton("종료하기");
        exitButton.setForeground(Color.RED);
        exitButton.addActionListener(e -> showExitConfirmation());

        giveUpButton = createMiniButton("포기하기");
        giveUpButton.addActionListener(e -> showGiveUpConfirmation());

        bottomLeftPanel.add(exitButton);
        bottomLeftPanel.add(giveUpButton);

        bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRightPanel.setOpaque(false);

        menuButton = createMiniButton("메뉴도감");
        menuButton.addActionListener(e -> openMenuGuide());

        bgmButton = new JToggleButton("BGM ON", true);
        bgmButton.setIcon(ImageManager.getImageIcon(ImageManager.BTN_MINI));
        bgmButton.setHorizontalTextPosition(JButton.CENTER);
        bgmButton.setVerticalTextPosition(JButton.CENTER);
        bgmButton.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        bgmButton.setForeground(Color.WHITE);
        bgmButton.setContentAreaFilled(false);
        bgmButton.setBorderPainted(false);
        bgmButton.setFocusPainted(false);
        bgmButton.setOpaque(false);
        bgmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgmButton.setPreferredSize(new Dimension(100, 40));

        bgmButton.addActionListener(e -> {
            if (bgmButton.isSelected()) {
                bgmButton.setText("BGM ON");
                BgmManager.setOn(true);
            } else {
                bgmButton.setText("BGM OFF");
                BgmManager.setOn(false);
            }
        });

        bottomRightPanel.add(menuButton);
        bottomRightPanel.add(bgmButton);

        bottomBarPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomBarPanel.add(bottomRightPanel, BorderLayout.EAST);
    }

    private JButton createMiniButton(String text) {
        JButton btn = new JButton(text);

        btn.setIcon(ImageManager.getImageIcon(ImageManager.BTN_MINI));
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 40));

        return btn;
    }

    public void showPanel(String panelName) {
        currentPanelName = panelName;
        cardLayout.show(mainPanel, panelName);

        if (panelName.equals("Order") || panelName.equals("Start") || panelName.equals("Nickname")) {
            bottomBarPanel.setVisible(false);
        } else {
            bottomBarPanel.setVisible(true);
        }

        if (panelName.equals("GameSpaceHub") || panelName.equals("MenuGuide")) {
            menuButton.setVisible(false);
        } else {
            menuButton.setVisible(true);
        }
    }

    private void openMenuGuide() {
        previousPanelName = currentPanelName;

        if ("Game".equals(currentPanelName)) {
            gameScreen.stopGame();
        }

        if (menuGuidePanel != null) {
            menuGuidePanel.resetToDefaultTab();
        }

        showPanel("MenuGuide");
    }

    private void handleMenuBack() {
        showPanel(previousPanelName);

        if ("Game".equals(previousPanelName)) {
            gameScreen.joinGame();
        }
    }

    private void initializeMenuItems() {
        allMenuItems = new ArrayList<>();

        List<MenuItem> loadedItems = MenuTextLoader.loadMenuData();

        if (!loadedItems.isEmpty()) {
            allMenuItems.addAll(loadedItems);
        } else {
            System.out.println("메뉴 파일 로드 실패. 기본 메뉴 사용.");
            allMenuItems.add(new MenuItem("아메리카노", MenuItem.MenuType.Coffee, true));
            allMenuItems.add(new MenuItem("카페 라떼", MenuItem.MenuType.Coffee, true));
        }

        allMenuItems.add(new MenuItem("치즈 케이크", MenuItem.MenuType.Dessert, true));
        allMenuItems.add(new MenuItem("초코 쿠키", MenuItem.MenuType.Dessert, true));
        allMenuItems.add(new MenuItem("마카롱", MenuItem.MenuType.Dessert, false));
    }

    private void addListenersToStartPanel(boolean hasSaveFile) {
        if (hasSaveFile) {
            startPanel.getContinueButton().addActionListener(e -> showPanel("GameSpaceHub"));
        }
        startPanel.getNewGameButton().addActionListener(e -> showPanel("Nickname"));
        startPanel.getExitButton().addActionListener(e -> showExitConfirmation());
    }

    private void addListenersToNewGamePanel() {
        newGamePanel.getStartButton().addActionListener(e -> {
            String cafeName = newGamePanel.getNameField().getText().trim();
            if (cafeName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "카페 이름을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            showPanel("GameSpaceHub");
        });
    }

    private void addListenersToGameSpacePanel() {
        gameSpacePanel.getBtn1().addActionListener(e -> {
            showPanel("Game");
            if (!gameScreen.isGameLoaded()) {
                gameScreen.startGame();
            } else {
                gameScreen.joinGame();
            }

        });

        gameSpacePanel.getBtn2().addActionListener(e -> openMenuGuide());

        gameSpacePanel.getBtn3().addActionListener(e -> showPanel("Graph"));

        if (gameSpacePanel.getBtn4() != null) {
            gameSpacePanel.getBtn4().addActionListener(e -> {
                showPanel("Search");
            });
        }
    }

    private void addListenersToGameScreen() {
        // [수정] ESC 키 바인딩 제거됨
        gameScreen.setDayEndListener((customerCount, revenue) -> {
            showDayEndDialog(customerCount, revenue);
        });
    }

    private void showExitConfirmation() {
        if ("Game".equals(currentPanelName)) {
            gameScreen.stopGame();
        }
        int confirmExit = JOptionPane.showConfirmDialog(
                this, "게임을 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION
        );
        if (confirmExit == JOptionPane.YES_OPTION) {
            try {
                FileWriter fw = new FileWriter(SAVE_FILE_PATH, false);
                fw.append(cafeName);
                fw.append(" ");
                fw.append(currentDayNumber + "");
                fw.close();
            } catch (IOException e) {
                System.out.println("게임 저장 실패");
                throw new RuntimeException(e);
            }

            System.exit(0);
        } else {
            if ("Game".equals(currentPanelName)) {
                gameScreen.joinGame();
            }
        }
    }

    private void showGiveUpConfirmation() {
        if ("Game".equals(currentPanelName)) {
            gameScreen.stopGame();
        }
        int confirmGiveUp = JOptionPane.showConfirmDialog(
                this, "정말 포기하고 처음으로 돌아가시겠습니까?\n(현재 진행 상황이 모두 초기화됩니다)",
                "포기 확인", JOptionPane.YES_NO_OPTION
        );
        if (confirmGiveUp == JOptionPane.YES_OPTION) {
            File save = new File(SAVE_FILE_PATH);
            // 관련 데이터 파일들도 삭제 (GameCanvas 상수가 있다면 사용, 없다면 주석 처리 필요)
            // File revenue = new File(core.GameCanvas.REVENUE_SAVE_PATH);
            // File sales = new File(core.GameCanvas.SALES_SAVE_PATH);

            if (save.exists()) {
                save.delete();
            }
            // if (revenue.exists()) revenue.delete();
            // if (sales.exists()) sales.delete();

            System.exit(0);
        } else {
            if ("Game".equals(currentPanelName)) {
                gameScreen.joinGame();
            }
        }
    }

    private void showDayEndDialog(int customerCount, int revenue) {
        gameScreen.stopGame();
        int dayNumber = currentDayNumber;

        totalAccumulatedRevenue += revenue;
        int netProfit = revenue;

        DaySummaryDialog dayEndDialog =
                new DaySummaryDialog(this, dayNumber, customerCount, revenue, totalAccumulatedRevenue);

        bottomBarPanel.setVisible(false);
        dayEndDialog.setVisible(true);
        bottomBarPanel.setVisible(true);

        dailySalesHistory.put(Integer.valueOf(dayNumber), Integer.valueOf(netProfit));

        currentDayNumber++;
        showPanel("GameSpaceHub");
    }

    private void initStatsService() {
        // StatsService 초기화 (GameCanvas 변수가 없으면 경로 문자열을 직접 써도 됩니다)
        // 예시: "sales_data.txt", "revenue_data.txt"
        try {
            // core.GameCanvas가 있다면 상수를 쓰시고, 없다면 직접 파일명을 적으세요.
            // 여기서는 일단 임시로 문자열을 직접 적어 오류를 방지합니다.
            statsService = StatsService.fromFiles(
                    "sales_data.txt",
                    "revenue_data.txt",
                    SAVE_FILE_PATH
            );
        } catch (Exception e) {
            statsService = new StatsService(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    null
            );
        }
    }
}
