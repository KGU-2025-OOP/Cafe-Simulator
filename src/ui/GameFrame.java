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

public class GameFrame extends JFrame
{
    private CardLayout m_cardLayout;
    private JPanel m_mainPanel;
    private StartPanel m_startPanel;
    private OrderPanel m_orderPanel;
    private NewGamePanel m_newGamePanel;
    private GameScreen m_gameScreen;
    private GameSpacePanel m_gameSpacePanel;
    private SalesGraphPanel m_salesGraphPanel;

    private int m_currentDaySales = 0;

    private int m_currentDayNumber;
    private Map<Integer, Integer> m_dailySalesHistory;

    private List<MenuItem> m_allMenuItems;

    private JPanel m_bgmPanel;
    private JToggleButton m_bgmButton;

    public GameFrame(boolean hasSaveFile)
    {
        setTitle("My Cafe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());

        m_currentDayNumber = 1;
        m_dailySalesHistory = new LinkedHashMap<>();

        InitializeMenuItems();

        m_cardLayout = new CardLayout();
        m_mainPanel = new JPanel(m_cardLayout);

        m_startPanel = new StartPanel(hasSaveFile);
        m_mainPanel.add(m_startPanel, "Start");

        Map<String, Integer> recipeData = CreateRecipeData();
        m_orderPanel = new OrderPanel(recipeData);
        m_mainPanel.add(m_orderPanel, "Order");

        m_newGamePanel = new NewGamePanel();
        m_mainPanel.add(m_newGamePanel, "Nickname");

        m_gameScreen = new GameScreen();
        m_mainPanel.add(m_gameScreen, "Game");

        m_gameSpacePanel = new GameSpacePanel();
        m_mainPanel.add(m_gameSpacePanel, "GameSpaceHub");

        m_salesGraphPanel = new SalesGraphPanel(m_dailySalesHistory);
        m_mainPanel.add(m_salesGraphPanel, "Graph");

        AddListenersToStartPanel(hasSaveFile);
        AddListenersToOrderPanel();
        AddListenersToNewGamePanel();
        AddListenersToGameScreen();
        AddListenersToGameSpacePanel();

        m_salesGraphPanel.getBackButton().addActionListener(e ->
        {
            ShowPanel("GameSpaceHub");
        });

        add(m_mainPanel, BorderLayout.CENTER);

        m_bgmPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        m_bgmButton = new JToggleButton("BGM ON", true);
        m_bgmButton.addActionListener(e ->
        {
            if (m_bgmButton.isSelected())
            {
                m_bgmButton.setText("BGM ON");
                System.out.println("BGM 켜기");
            }
            else
            {
                m_bgmButton.setText("BGM OFF");
                System.out.println("BGM 끄기");
            }
        });
        m_bgmPanel.add(m_bgmButton);
        add(m_bgmPanel, BorderLayout.SOUTH);
    }

    private void ShowPanel(String panelName)
    {
        m_cardLayout.show(m_mainPanel, panelName);

        if (panelName.equals("Order"))
        {
            m_bgmPanel.setVisible(false);
        }
        else
        {
            m_bgmPanel.setVisible(true);
        }
    }

    private void InitializeMenuItems()
    {
        m_allMenuItems = new ArrayList<>();
        m_allMenuItems.add(new MenuItem("아메리카노", MenuItem.MenuType.Beverage, true));
        m_allMenuItems.add(new MenuItem("카페 라떼", MenuItem.MenuType.Beverage, true));
        m_allMenuItems.add(new MenuItem("바닐라 라떼", MenuItem.MenuType.Beverage, false));
        m_allMenuItems.add(new MenuItem("딸기 라떼", MenuItem.MenuType.Beverage, false));
        m_allMenuItems.add(new MenuItem("치즈 케이크", MenuItem.MenuType.Dessert, true));
        m_allMenuItems.add(new MenuItem("초코 쿠키", MenuItem.MenuType.Dessert, true));
        m_allMenuItems.add(new MenuItem("마카롱", MenuItem.MenuType.Dessert, false));
    }

    private Map<String, Integer> CreateRecipeData()
    {
        Map<String, Integer> recipeData = new LinkedHashMap<>();
        recipeData.put("원두", Integer.valueOf(1000));
        recipeData.put("우유", Integer.valueOf(500));
        recipeData.put("시럽", Integer.valueOf(300));
        return recipeData;
    }

    private void AddListenersToStartPanel(boolean hasSaveFile)
    {
        if (hasSaveFile)
        {
            m_startPanel.getContinueButton().addActionListener(e ->
            {
                ShowPanel("GameSpaceHub");
            });
        }
        m_startPanel.getNewGameButton().addActionListener(e ->
        {
            System.out.println("시작/새로하기 버튼 클릭됨");
            ShowPanel("Nickname");
        });
        m_startPanel.getExitButton().addActionListener(e ->
        {
            ShowExitConfirmation();
        });
    }

    private void AddListenersToOrderPanel()
    {
        m_orderPanel.GetConfirmButton().addActionListener(e ->
        {
            int totalCost = m_orderPanel.GetTotalCost();
            m_currentDaySales = -totalCost;

            System.out.println("발주가 확정되었습니다.");

            m_gameScreen.SetDayLabel(m_currentDayNumber + "일차");

            ShowPanel("Game");
        });
        m_orderPanel.GetCancelButton().addActionListener(e ->
        {
            System.out.println("발주가 취소되었습니다.");
            ShowPanel("GameSpaceHub");
        });
    }

    private void AddListenersToNewGamePanel()
    {
        m_newGamePanel.GetStartButton().addActionListener(e ->
        {
            String cafeName = m_newGamePanel.GetNameField().getText().trim();

            if (cafeName.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "카페 이름을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("카페 이름 (" + cafeName + ") 확정 -> 메인 허브로 이동");

            ShowPanel("GameSpaceHub");
        });
    }

    private void AddListenersToGameSpacePanel()
    {
        m_gameSpacePanel.getBtn1().addActionListener(e ->
        {
            System.out.println("운영시작 버튼 클릭됨 -> 발주 화면으로");

            m_orderPanel.ResetSpinners();

            ShowPanel("Order");
        });

        m_gameSpacePanel.getBtn2().addActionListener(e ->
        {
            System.out.println("메뉴도감 버튼 클릭됨");
            ShowMenuGuideFromHub();
        });

        m_gameSpacePanel.getBtn3().addActionListener(e ->
        {
            System.out.println("성장도그래프 버튼 클릭됨");
            ShowPanel("Graph");
        });
    }

    private void AddListenersToGameScreen()
    {
        AddExitBinding(m_startPanel);
        AddExitBinding(m_newGamePanel);
        AddExitBinding(m_orderPanel);
        AddPauseBinding(m_gameScreen);
        AddExitBinding(m_gameSpacePanel);
        AddExitBinding(m_salesGraphPanel);

        m_gameScreen.GetEndDayButton().addActionListener(e ->
        {
            ShowDayEndDialog();
        });
    }

    private void AddExitBinding(JPanel panel)
    {
        String actionKey = "showExitConfirmation";
        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        inputMap.put(escKeyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ShowExitConfirmation();
            }
        });
    }

    private void AddPauseBinding(JPanel panel)
    {
        String actionKey = "showPauseMenu";
        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        inputMap.put(escKeyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("ESC 키 감지됨! (일시정지)");
                ShowPauseDialog();
            }
        });
    }

    private void ShowExitConfirmation()
    {
        int confirmExit = JOptionPane.showConfirmDialog(
                this, "게임을 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION
        );
        if (confirmExit == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    private void ShowPauseDialog()
    {
        PauseDialog pauseDialog = new PauseDialog(this);
        pauseDialog.setVisible(true);
        PauseDialog.PauseResult choice = pauseDialog.getResult();

        switch (choice)
        {
            case CALENDAR:
                System.out.println("달력 버튼 클릭됨");
                ShowCalendarDialog();
                break;
            case MENU:
                System.out.println("메뉴판 버튼 클릭됨");
                ShowMenuDialog();
                break;
            case GIVE_UP:
                System.out.println("포기하기 버튼 클릭됨");
                int confirmGiveUp = JOptionPane.showConfirmDialog(
                        this, "정말 포기하고 처음으로 돌아가시겠습니까?", "포기 확인", JOptionPane.YES_NO_OPTION
                );
                if (confirmGiveUp == JOptionPane.YES_OPTION)
                {
                    System.out.println("데이터 초기화... 시작 화면으로 돌아갑니다.");
                    m_currentDayNumber = 1;
                    m_dailySalesHistory.clear();
                    ShowPanel("Start");
                }
                break;
            case EXIT:
                System.out.println("종료하기 버튼 클릭됨");
                ShowExitConfirmation();
                break;
            case CONTINUE:
            case NONE:
            default:
                System.out.println("계속하기 버튼 클릭됨 (게임 재개)");
                break;
        }
    }

    private void ShowCalendarDialog()
    {
        CalendarDialog calendarDialog = new CalendarDialog(this, m_currentDayNumber, m_dailySalesHistory);

        m_bgmPanel.setVisible(false);
        calendarDialog.setVisible(true);
        m_bgmPanel.setVisible(true);

        if (calendarDialog.ShouldReopenPause())
        {
            ShowPauseDialog();
        }
    }

    private void ShowMenuDialog()
    {
        MenuDialog menuDialog = new MenuDialog(this, m_allMenuItems);

        m_bgmPanel.setVisible(false);
        menuDialog.setVisible(true);
        m_bgmPanel.setVisible(true);

        if (menuDialog.ShouldReopenPause())
        {
            ShowPauseDialog();
        }
    }

    private void ShowMenuGuideFromHub()
    {
        MenuDialog menuDialog = new MenuDialog(this, m_allMenuItems);

        m_bgmPanel.setVisible(false);
        menuDialog.setVisible(true);
        m_bgmPanel.setVisible(true);
    }

    private void ShowDayEndDialog()
    {
        int dayNumber = m_currentDayNumber;

        System.out.println(dayNumber + "일차 장사를 마감합니다.");

        Random rand = new Random();
        int customerCount = 10 + rand.nextInt(15);
        int revenue = customerCount * (3000 + rand.nextInt(2000));
        int orderCost = (m_currentDaySales == 0) ? 0 : -m_currentDaySales;

        DayEndDialog dayEndDialog = new DayEndDialog(this, dayNumber, customerCount, revenue, orderCost);

        m_bgmPanel.setVisible(false);
        dayEndDialog.setVisible(true);
        m_bgmPanel.setVisible(true);

        System.out.println(dayNumber + "일차 결산 완료.");

        int netProfit = revenue - orderCost;

        Integer todayKey = Integer.valueOf(dayNumber);

        m_dailySalesHistory.put(todayKey, Integer.valueOf(netProfit));

        System.out.println(dayNumber + "일차 순수익 " + netProfit + "원 저장됨.");

        m_currentDayNumber++;

        m_currentDaySales = 0;

        ShowPanel("GameSpaceHub");
    }

    public static void main(String[] args)
    {
        final boolean MOCK_SAVE_FILE_EXISTS = false;
        SwingUtilities.invokeLater(() ->
        {
            GameFrame game = new GameFrame(MOCK_SAVE_FILE_EXISTS);
            game.setVisible(true);
        });
    }
}