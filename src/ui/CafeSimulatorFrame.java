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
import javax.swing.JToggleButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class CafeSimulatorFrame extends JFrame {
	
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private StartMenuPanel startPanel;
    private OrderManagementPanel orderPanel;
    private NewGameSetupPanel newGamePanel;
    private GameplayPanel gameScreen;
    private GameplayAreaPanel gameSpacePanel;
    private SalesStatisticsPanel salesGraphPanel;

    private int currentDaySales = 0;

    private int currentDayNumber;
    private Map<Integer, Integer> dailySalesHistory;

    private List<MenuItem> allMenuItems;

    private JPanel bgmPanel;
    private JToggleButton bgmButton;

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

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());

        currentDayNumber = 1;
        dailySalesHistory = new LinkedHashMap<>();

        initializeMenuItems();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        startPanel = new StartMenuPanel(hasSaveFile);
        mainPanel.add(startPanel, "Start");

        Map<String, Integer> recipeData = createRecipeData();
        orderPanel = new OrderManagementPanel(recipeData);
        mainPanel.add(orderPanel, "Order");

        newGamePanel = new NewGameSetupPanel();
        mainPanel.add(newGamePanel, "Nickname");

        gameScreen = new GameplayPanel();
        mainPanel.add(gameScreen, "Game");

        gameSpacePanel = new GameplayAreaPanel();
        mainPanel.add(gameSpacePanel, "GameSpaceHub");

        salesGraphPanel = new SalesStatisticsPanel(dailySalesHistory);
        mainPanel.add(salesGraphPanel, "Graph");

        addListenersToStartPanel(hasSaveFile);
        addListenersToOrderPanel();
        addListenersToNewGamePanel();
        addListenersToGameScreen();
        addListenersToGameSpacePanel();

        salesGraphPanel.getBackButton().addActionListener(e -> {
            showPanel("GameSpaceHub");
        });

        add(mainPanel, BorderLayout.CENTER);

        bgmPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bgmButton = new JToggleButton("BGM ON", true);
        bgmButton.addActionListener(e -> {
            if (bgmButton.isSelected()) {
                bgmButton.setText("BGM ON");
                System.out.println("BGM 켜기");
            }
            else {
                bgmButton.setText("BGM OFF");
                System.out.println("BGM 끄기");
            }
        });
        
        bgmPanel.add(bgmButton);
        
        add(bgmPanel, BorderLayout.SOUTH);
    }

    private void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);

        if (panelName.equals("Order")) {
            bgmPanel.setVisible(false);
        }
        else {
            bgmPanel.setVisible(true);
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

    private Map<String, Integer> createRecipeData() {
        Map<String, Integer> recipeData = new LinkedHashMap<>();
        
        recipeData.put("원두", Integer.valueOf(1000));
        recipeData.put("우유", Integer.valueOf(500));
        recipeData.put("시럽", Integer.valueOf(300));
        
        return recipeData;
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

    private void addListenersToOrderPanel() {
        orderPanel.getConfirmButton().addActionListener(e -> {
            int totalCost = orderPanel.getTotalCost();
            currentDaySales = -totalCost;

            System.out.println("발주가 확정되었습니다.");

            gameScreen.setDayLabel(currentDayNumber + "일차");

            showPanel("Game");
        });
        
        orderPanel.getCancelButton().addActionListener(e -> {
            System.out.println("발주가 취소되었습니다.");
            showPanel("GameSpaceHub");
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
            System.out.println("운영시작 버튼 클릭됨 -> 발주 화면으로");

            orderPanel.resetSpinners();

            showPanel("Order");
        });

        gameSpacePanel.getBtn2().addActionListener(e -> {
            System.out.println("메뉴도감 버튼 클릭됨");
            showMenuGuideFromHub();
        });

        gameSpacePanel.getBtn3().addActionListener(e -> {
            System.out.println("성장도그래프 버튼 클릭됨");
            showPanel("Graph");
        });
    }

    private void addListenersToGameScreen() {
        addExitBinding(startPanel);
        addExitBinding(newGamePanel);
        addExitBinding(orderPanel);
        addPauseBinding(gameScreen);
        addExitBinding(gameSpacePanel);
        addExitBinding(salesGraphPanel);

        gameScreen.getEndDayButton().addActionListener(e -> {
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

    private void showPauseDialog() {
        PauseMenuDialog pauseDialog = new PauseMenuDialog(this);
        pauseDialog.setVisible(true);
        PauseMenuDialog.PauseResult choice = pauseDialog.getResult();

        switch (choice) {
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
                    currentDayNumber = 1;
                    dailySalesHistory.clear();
                    showPanel("Start");
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

    private void showCalendarDialog() {
        CalendarViewDialog calendarDialog = new CalendarViewDialog(this, currentDayNumber, dailySalesHistory);

        bgmPanel.setVisible(false);
        calendarDialog.setVisible(true);
        bgmPanel.setVisible(true);

        if (calendarDialog.shouldReopenPause()) {
            showPauseDialog();
        }
    }

    private void showMenuDialog() {
        MenuDialog menuDialog = new MenuDialog(this, allMenuItems);

        bgmPanel.setVisible(false);
        menuDialog.setVisible(true);
        bgmPanel.setVisible(true);

        if (menuDialog.shouldReopenPause()) {
            showPauseDialog();
        }
    }

    private void showMenuGuideFromHub() {
        MenuDialog menuDialog = new MenuDialog(this, allMenuItems);

        bgmPanel.setVisible(false);
        menuDialog.setVisible(true);
        bgmPanel.setVisible(true);
    }

    private void showDayEndDialog() {
        int dayNumber = currentDayNumber;

        System.out.println(dayNumber + "일차 장사를 마감합니다.");

        Random rand = new Random();
        int customerCount = 10 + rand.nextInt(15);
        int revenue = customerCount * (3000 + rand.nextInt(2000));
        int orderCost = (currentDaySales == 0) ? 0 : -currentDaySales;

        DaySummaryDialog dayEndDialog = new DaySummaryDialog(this, dayNumber, customerCount, revenue, orderCost);

        bgmPanel.setVisible(false);
        dayEndDialog.setVisible(true);
        bgmPanel.setVisible(true);

        System.out.println(dayNumber + "일차 결산 완료.");

        int netProfit = revenue - orderCost;

        Integer todayKey = Integer.valueOf(dayNumber);

        dailySalesHistory.put(todayKey, Integer.valueOf(netProfit));

        System.out.println(dayNumber + "일차 순수익 " + netProfit + "원 저장됨.");

        currentDayNumber++;

        currentDaySales = 0;

        showPanel("GameSpaceHub");
    }
}