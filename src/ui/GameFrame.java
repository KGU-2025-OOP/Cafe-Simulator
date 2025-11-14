package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class GameFrame extends JFrame
{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private StartPanel startPanel;
    private OrderPanel orderPanel;
    private NewGamePanel newGamePanel;
    private GameScreen gameScreen;
    private GameSpacePanel gameSpacePanel;
    private JPanel tempGraphPanel;

    private int currentDaySales = 0;
    private LocalDate currentSystemDate;
    private Map<String, Integer> dailySalesHistory;
    private List<MenuItem> allMenuItems;

    private static final LocalDate START_DATE = LocalDate.of(2025, 11, 13);

    public GameFrame(boolean hasSaveFile)
    {
        setTitle("My Cafe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.currentSystemDate = START_DATE;
        this.dailySalesHistory = new LinkedHashMap<>();
        // 1일차 10000원 초기 자본금 삭제 (이전 요청)
        // String todayString = START_DATE.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // this.dailySalesHistory.put(todayString, Integer.valueOf(10000));

        initializeMenuItems();

        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(this.cardLayout);

        // 1. StartPanel
        this.startPanel = new StartPanel(hasSaveFile);
        this.mainPanel.add(this.startPanel, "Start");

        // 2. OrderPanel
        Map<String, Integer> recipeData = createRecipeData();
        this.orderPanel = new OrderPanel(recipeData);
        this.mainPanel.add(this.orderPanel, "Order");

        // 3. NewGamePanel
        this.newGamePanel = new NewGamePanel();
        this.mainPanel.add(this.newGamePanel, "Nickname");

        // 4. GameScreen
        this.gameScreen = new GameScreen();
        this.mainPanel.add(this.gameScreen, "Game");

        // 5. GameSpacePanel (메인 허브)
        this.gameSpacePanel = new GameSpacePanel();
        this.mainPanel.add(this.gameSpacePanel, "GameSpaceHub");

        // 6. 임시 그래프 패널
        this.tempGraphPanel = createTempGraphPanel();
        this.mainPanel.add(this.tempGraphPanel, "Graph");

        // 7. 리스너 연결
        addListenersToStartPanel(hasSaveFile);
        addListenersToOrderPanel();
        addListenersToNewGamePanel();
        addListenersToGameScreen();
        addListenersToGameSpacePanel();

        add(this.mainPanel);
    }

    private void initializeMenuItems()
    {
        this.allMenuItems = new ArrayList<>();
        this.allMenuItems.add(new MenuItem("아메리카노", MenuItem.MenuType.BEVERAGE, true));
        this.allMenuItems.add(new MenuItem("카페 라떼", MenuItem.MenuType.BEVERAGE, true));
        this.allMenuItems.add(new MenuItem("바닐라 라떼", MenuItem.MenuType.BEVERAGE, false));
        this.allMenuItems.add(new MenuItem("딸기 라떼", MenuItem.MenuType.BEVERAGE, false));
        this.allMenuItems.add(new MenuItem("치즈 케이크", MenuItem.MenuType.DESSERT, true));
        this.allMenuItems.add(new MenuItem("초코 쿠키", MenuItem.MenuType.DESSERT, true));
        this.allMenuItems.add(new MenuItem("마카롱", MenuItem.MenuType.DESSERT, false));
    }

    private Map<String, Integer> createRecipeData()
    {
        Map<String, Integer> recipeData = new LinkedHashMap<>();
        recipeData.put("원두", Integer.valueOf(1000));
        recipeData.put("우유", Integer.valueOf(500));
        recipeData.put("시럽", Integer.valueOf(300));
        return recipeData;
    }

    private JPanel createTempGraphPanel() {
        JPanel panel = new JPanel(new java.awt.GridBagLayout());
        panel.add(new JLabel("성장도 그래프 (임시 화면입니다)"));

        JButton backButton = new JButton("돌아가기");
        backButton.addActionListener(e -> {
            this.cardLayout.show(this.mainPanel, "GameSpaceHub");
        });

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(20, 0, 0, 0);
        panel.add(backButton, gbc);

        return panel;
    }

    private void addListenersToStartPanel(boolean hasSaveFile)
    {
        if (hasSaveFile) {
            this.startPanel.getContinueButton().addActionListener(e -> {
                this.cardLayout.show(this.mainPanel, "GameSpaceHub");
            });
        }
        this.startPanel.getNewGameButton().addActionListener(e -> {
            System.out.println("시작/새로하기 버튼 클릭됨");
            this.cardLayout.show(this.mainPanel, "Nickname");
        });
        this.startPanel.getExitButton().addActionListener(e -> {
            showExitConfirmation();
        });
        this.startPanel.getBgmButton().addActionListener(e -> {
            System.out.println("BGM 버튼 클릭됨");
        });
    }

    private void addListenersToOrderPanel()
    {
        this.orderPanel.getConfirmButton().addActionListener(e -> {
            int totalCost = this.orderPanel.getTotalCost();
            this.currentDaySales = -totalCost;

            System.out.println("발주가 확정되었습니다.");

            int dayNumber = (int) ChronoUnit.DAYS.between(START_DATE, this.currentSystemDate) + 1;
            this.gameScreen.setDayLabel(dayNumber + "일차");

            this.cardLayout.show(this.mainPanel, "Game");
        });
        this.orderPanel.getCancelButton().addActionListener(e -> {
            System.out.println("발주가 취소되었습니다.");
            this.cardLayout.show(this.mainPanel, "GameSpaceHub");
        });
    }

    private void addListenersToNewGamePanel() {
        this.newGamePanel.getStartButton().addActionListener(e -> {
            String cafeName = this.newGamePanel.getNameField().getText().trim();

            if (cafeName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "카페 이름을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("카페 이름 (" + cafeName + ") 확정 -> 메인 허브로 이동");

            this.cardLayout.show(this.mainPanel, "GameSpaceHub");
        });
    }

    private void addListenersToGameSpacePanel() {
        this.gameSpacePanel.getBtn1().addActionListener(e -> {
            System.out.println("운영시작 버튼 클릭됨 -> 발주 화면으로");

            // 발주 패널 리셋
            this.orderPanel.resetSpinners();

            this.cardLayout.show(this.mainPanel, "Order");
        });

        this.gameSpacePanel.getBtn2().addActionListener(e -> {
            System.out.println("메뉴도감 버튼 클릭됨");
            showMenuGuideFromHub();
        });

        this.gameSpacePanel.getBtn3().addActionListener(e -> {
            System.out.println("성장도그래프 버튼 클릭됨");
            this.cardLayout.show(this.mainPanel, "Graph");
        });
    }

    private void addListenersToGameScreen() {
        addExitBinding(this.startPanel);
        addExitBinding(this.newGamePanel);
        addExitBinding(this.orderPanel);
        addPauseBinding(this.gameScreen);
        addExitBinding(this.gameSpacePanel);
        addExitBinding(this.tempGraphPanel);

        this.gameScreen.getEndDayButton().addActionListener(e -> {
            showDayEndDialog();
        });
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

    private void addPauseBinding(JPanel panel) {
        String actionKey = "showPauseMenu";
        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        inputMap.put(escKeyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ESC 키 감지됨! (일시정지)");
                showPauseDialog();
            }
        });
    }

    private void showExitConfirmation() {
        int confirmExit = JOptionPane.showConfirmDialog(
                this, "게임을 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION
        );
        if (confirmExit == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showPauseDialog()
    {
        PauseDialog pauseDialog = new PauseDialog(this);
        pauseDialog.setVisible(true);
        PauseDialog.PauseResult choice = pauseDialog.getResult();

        switch (choice)
        {
            case CALENDAR:
                System.out.println("달력 버튼 클릭됨");
                showCalendarDialog();
                break;
            case MENU:
                System.out.println("메뉴판 버튼 클릭됨");
                showMenuDialog();
                break;
            case GIVE_UP:
                System.out.println("포기하기 버튼 클릭됨");
                int confirmGiveUp = JOptionPane.showConfirmDialog(
                        this, "정말 포기하고 처음으로 돌아가시겠습니까?", "포기 확인", JOptionPane.YES_NO_OPTION
                );
                if (confirmGiveUp == JOptionPane.YES_OPTION) {
                    System.out.println("데이터 초기화... 시작 화면으로 돌아갑니다.");
                    this.cardLayout.show(this.mainPanel, "Start");
                }
                break;
            case EXIT:
                System.out.println("종료하기 버튼 클릭됨");
                showExitConfirmation();
                break;
            case CONTINUE:
            case NONE:
            default:
                System.out.println("계속하기 버튼 클릭됨 (게임 재개)");
                break;
        }
    }

    private void showCalendarDialog()
    {
        CalendarDialog calendarDialog = new CalendarDialog(this, this.currentSystemDate, this.dailySalesHistory);
        calendarDialog.setVisible(true);

        if (calendarDialog.shouldReopenPause()) {
            showPauseDialog();
        }
    }

    private void showMenuDialog()
    {
        MenuDialog menuDialog = new MenuDialog(this, this.allMenuItems);
        menuDialog.setVisible(true);

        if (menuDialog.shouldReopenPause()) {
            showPauseDialog();
        }
    }

    private void showMenuGuideFromHub()
    {
        MenuDialog menuDialog = new MenuDialog(this, this.allMenuItems);
        menuDialog.setVisible(true);
    }

    private void showDayEndDialog() {
        // 1. 현재 며칠차인지 계산
        int dayNumber = (int) ChronoUnit.DAYS.between(START_DATE, this.currentSystemDate) + 1;

        // [수정] 날짜 대신 'N일차'로 콘솔 출력
        System.out.println(dayNumber + "일차 장사를 마감합니다.");

        // 2. 임시 (가상) 데이터 생성
        Random rand = new Random();
        int customerCount = 10 + rand.nextInt(15);
        int revenue = customerCount * (3000 + rand.nextInt(2000));
        int orderCost = (this.currentDaySales == 0) ? 0 : -this.currentDaySales;

        // 3. 결산 다이얼로그 생성 및 표시
        DayEndDialog dayEndDialog = new DayEndDialog(this, dayNumber, customerCount, revenue, orderCost);
        dayEndDialog.setVisible(true);

        // 4. (다이얼로그가 닫힌 후) 다음 날 준비
        System.out.println(dayNumber + "일차 결산 완료.");

        // 5. 하루 매출을 누적
        int netProfit = revenue - orderCost;
        String todayKey = this.currentSystemDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (dayNumber > 1) {
            this.dailySalesHistory.put(todayKey, Integer.valueOf(netProfit));
        } else {
            int initialCapital = this.dailySalesHistory.getOrDefault(todayKey, Integer.valueOf(0));
            this.dailySalesHistory.put(todayKey, Integer.valueOf(initialCapital + netProfit));
        }
        // [수정] 날짜 대신 'N일차'로 콘솔 출력
        System.out.println(dayNumber + "일차 순수익 " + netProfit + "원 저장됨.");

        // 6. 다음 날로 이동
        this.currentSystemDate = this.currentSystemDate.plusDays(1);
        this.currentDaySales = 0;

        // 7. 메인 허브로 복귀
        this.cardLayout.show(this.mainPanel, "GameSpaceHub");
    }

    public static void main(String[] args)
    {
        final boolean MOCK_SAVE_FILE_EXISTS = false;
        SwingUtilities.invokeLater(() -> {
            GameFrame game = new GameFrame(MOCK_SAVE_FILE_EXISTS);
            game.setVisible(true);
        });
    }
}