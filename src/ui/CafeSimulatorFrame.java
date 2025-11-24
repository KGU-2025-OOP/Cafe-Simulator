package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
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
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class CafeSimulatorFrame extends JFrame {

    private static final int MIN_CUSTOMERS = 10;
    private static final int MAX_EXTRA_CUSTOMERS = 15;
    private static final int AVG_SPEND_PER_CUSTOMER = 3000;
    private static final int SPEND_VARIANCE = 2000;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // 배경 이미지
    private Image mainBackgroundImage;

    private StartMenuPanel startPanel;
    private NewGameSetupPanel newGamePanel;
    private GameplayPanel gameScreen;
    private GameplayAreaPanel gameSpacePanel;
    private SalesStatisticsPanel salesGraphPanel;
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

    public static void mymain(String[] args) {
        final boolean MOCK_SAVE_FILE_EXISTS = false;
        SwingUtilities.invokeLater(() -> {
            CafeSimulatorFrame game = new CafeSimulatorFrame(MOCK_SAVE_FILE_EXISTS);
            game.setVisible(true);
        });
    }

    public CafeSimulatorFrame(boolean hasSaveFile) {
        setTitle("My Cafe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // [수정] ImageManager를 통해 배경 이미지 로드
        mainBackgroundImage = ImageManager.getImage(ImageManager.IMG_MAIN_BG);

        setLayout(new BorderLayout());

        currentDayNumber = 1;
        dailySalesHistory = new LinkedHashMap<>();
        totalAccumulatedRevenue = 0;

        initializeMenuItems();

        cardLayout = new CardLayout();

        // [수정] 배경 그리기 로직 포함된 익명 클래스
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

        newGamePanel = new NewGameSetupPanel();
        mainPanel.add(newGamePanel, "Nickname");

        gameScreen = new GameplayPanel();
        mainPanel.add(gameScreen, "Game");

        gameSpacePanel = new GameplayAreaPanel();
        mainPanel.add(gameSpacePanel, "GameSpaceHub");

        salesGraphPanel = new SalesStatisticsPanel(dailySalesHistory);
        mainPanel.add(salesGraphPanel, "Graph");

        menuGuidePanel = new MenuGuidePanel(allMenuItems, this::handleMenuBack);
        mainPanel.add(menuGuidePanel, "MenuGuide");

        addListenersToStartPanel(hasSaveFile);
        addListenersToNewGamePanel();
        addListenersToGameScreen();
        addListenersToGameSpacePanel();

        salesGraphPanel.getBackButton().addActionListener(e -> {
            showPanel("GameSpaceHub");
        });

        add(mainPanel, BorderLayout.CENTER);

        createBottomBar();
        add(bottomBarPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        showPanel("Start");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            }
        });
    }

    private void createBottomBar() {
        bottomBarPanel = new JPanel(new BorderLayout());

        bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Font btnFont = new Font("Malgun Gothic", Font.BOLD, 14);

        exitButton = new JButton("종료하기");
        exitButton.setFont(btnFont);
        exitButton.setForeground(Color.RED);
        exitButton.addActionListener(e -> showExitConfirmation());

        giveUpButton = new JButton("포기하기");
        giveUpButton.setFont(btnFont);
        giveUpButton.addActionListener(e -> showGiveUpConfirmation());

        bottomLeftPanel.add(exitButton);
        bottomLeftPanel.add(giveUpButton);

        bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        menuButton = new JButton("메뉴도감");
        menuButton.setFont(btnFont);
        menuButton.addActionListener(e -> openMenuGuide());

        bgmButton = new JToggleButton("BGM ON", true);
        bgmButton.setFont(btnFont);
        bgmButton.addActionListener(e -> {
            if (bgmButton.isSelected()) {
                bgmButton.setText("BGM ON");
                System.out.println("BGM 켜기");
            } else {
                bgmButton.setText("BGM OFF");
                System.out.println("BGM 끄기");
            }
        });

        bottomRightPanel.add(menuButton);
        bottomRightPanel.add(bgmButton);

        bottomBarPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomBarPanel.add(bottomRightPanel, BorderLayout.EAST);
    }

    private void openMenuGuide() {
        previousPanelName = currentPanelName;

        if ("Game".equals(currentPanelName)) {
            gameScreen.stopGame();
            System.out.println("게임 중 메뉴 열기 -> 게임 중지");
        }

        showPanel("MenuGuide");
    }

    private void handleMenuBack() {
        showPanel(previousPanelName);

        if ("Game".equals(previousPanelName)) {
            System.out.println("메뉴 닫힘 -> 게임 재시작");
            gameScreen.startGame();
        }
    }

    public void showPanel(String panelName) {
        currentPanelName = panelName;
        cardLayout.show(mainPanel, panelName);

        if (panelName.equals("Order")) {
            bottomBarPanel.setVisible(false);
        } else if (panelName.equals("Start") || panelName.equals("Nickname")) {
            bottomBarPanel.setVisible(false);
        } else {
            bottomBarPanel.setVisible(true);
        }
    }

    private void initializeMenuItems() {
        allMenuItems = new ArrayList<>();
        allMenuItems.add(new MenuItem("아메리카노", MenuItem.MenuType.Beverage, true));
        allMenuItems.add(new MenuItem("카페 라떼", MenuItem.MenuType.Beverage, true));
        allMenuItems.add(new MenuItem("바닐라 라떼", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("딸기 라떼", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Beverage, false));
        allMenuItems.add(new MenuItem("치즈 케이크", MenuItem.MenuType.Dessert, true));
        allMenuItems.add(new MenuItem("초코 쿠키", MenuItem.MenuType.Dessert, true));
        allMenuItems.add(new MenuItem("마카롱", MenuItem.MenuType.Dessert, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Dessert, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Dessert, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Dessert, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Dessert, false));
        allMenuItems.add(new MenuItem("스크롤용", MenuItem.MenuType.Dessert, false));
    }

    private void addListenersToStartPanel(boolean hasSaveFile) {
        if (hasSaveFile) {
            startPanel.getContinueButton().addActionListener(e -> {
                showPanel("GameSpaceHub");
            });
        }
        startPanel.getNewGameButton().addActionListener(e -> {
            System.out.println("시작/새로하기 버튼 클릭됨");
            showPanel("Nickname");
        });
        startPanel.getExitButton().addActionListener(e -> {
            showExitConfirmation();
        });
    }

    private void addListenersToNewGamePanel() {
        newGamePanel.getStartButton().addActionListener(e -> {
            String cafeName = newGamePanel.getNameField().getText().trim();
            if (cafeName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "카페 이름을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            System.out.println("카페 이름 (" + cafeName + ") 확정 -> 메인 허브로 이동");
            showPanel("GameSpaceHub");
        });
    }

    private void addListenersToGameSpacePanel() {
        gameSpacePanel.getBtn1().addActionListener(e -> {
            System.out.println("운영시작 버튼 클릭됨 -> 게임 화면으로");
            gameScreen.setDayLabel(currentDayNumber + "일차");
            showPanel("Game");
            gameScreen.startGame();
        });

        gameSpacePanel.getBtn2().addActionListener(e -> openMenuGuide());

        gameSpacePanel.getBtn3().addActionListener(e -> showPanel("Graph"));
    }

    private void addListenersToGameScreen() {
        addExitBinding(startPanel);
        addExitBinding(newGamePanel);
        addExitBinding(gameSpacePanel);
        addExitBinding(salesGraphPanel);
        gameScreen.getEndDayButton().addActionListener(e -> showDayEndDialog());
    }

    private void addExitBinding(JPanel panel) {
        String actionKey = "showExitConfirmation";
        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        inputMap.put(escKeyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExitConfirmation();
            }
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
            System.exit(0);
        } else {
            if ("Game".equals(currentPanelName)) {
                gameScreen.startGame();
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
            System.out.println("데이터 초기화... 시작 화면으로 돌아갑니다.");

            currentDayNumber = 1;
            dailySalesHistory.clear();
            totalAccumulatedRevenue = 0;
            showPanel("Start");
        } else {
            if ("Game".equals(currentPanelName)) {
                gameScreen.startGame();
            }
        }
    }

    private void showMenuGuideFromHub() {
        openMenuGuide();
    }

    private void showDayEndDialog() {
        gameScreen.stopGame();

        int dayNumber = currentDayNumber;

        System.out.println(dayNumber + "일차 장사를 마감합니다.");

        Random rand = new Random();
        int customerCount = MIN_CUSTOMERS + rand.nextInt(MAX_EXTRA_CUSTOMERS);
        int revenue = customerCount * (AVG_SPEND_PER_CUSTOMER + rand.nextInt(SPEND_VARIANCE));

        totalAccumulatedRevenue += revenue;

        int netProfit = revenue;

        DaySummaryDialog dayEndDialog = new DaySummaryDialog(this, dayNumber, customerCount, revenue, totalAccumulatedRevenue);

        bottomBarPanel.setVisible(false);
        dayEndDialog.setVisible(true);
        bottomBarPanel.setVisible(true);

        dailySalesHistory.put(Integer.valueOf(dayNumber), Integer.valueOf(netProfit));

        System.out.println(dayNumber + "일차 순수익 " + netProfit + "원 저장됨.");

        currentDayNumber++;

        showPanel("GameSpaceHub");
    }
}