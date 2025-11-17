package ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

public class MenuDialog extends JDialog
{
    private boolean m_shouldReopenPause = false;

    // 가운데에 실제 컨텐츠(음료/베이커리 패널)를 바꿔끼우는 컨테이너
    private JPanel m_contentContainer;

    public MenuDialog(JFrame parent, List<MenuItem> allMenus)
    {
        super(parent, "메뉴 도감", true);

        // ====== 전체 배경 패널 (전체 화면 채우는 컨테이너) ======
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(new Color(240, 240, 240));
        backPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // --------------------------------------------------
        // ⬆ 상단 바: 뒤로가기 / 제목 / 카테고리 버튼
        // --------------------------------------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // ← 뒤로가기 버튼 영역
        JPanel backwardPanel = new JPanel(new BorderLayout());
        backwardPanel.setOpaque(false);
        backwardPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JButton backwardButton = new JButton("<-");
        backwardButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        backwardButton.addActionListener(e ->
        {
            m_shouldReopenPause = true;
            dispose();
        });

        backwardPanel.add(backwardButton, BorderLayout.CENTER);
        topPanel.add(backwardPanel, BorderLayout.WEST);

        // 가운데 제목
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("메뉴 도감");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.CENTER);

        // 오른쪽 카테고리 버튼들 (음료 / 베이커리)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setOpaque(false);
        JButton drinkButton = new JButton("음료");
        JButton bakeryButton = new JButton("베이커리");

        buttonPanel.add(drinkButton);
        buttonPanel.add(bakeryButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // 상단 바를 북쪽에 추가
        backPanel.add(topPanel, BorderLayout.NORTH);

        // --------------------------------------------------
        // ⬇ 중앙 컨텐츠 컨테이너 (여기에 DRINK/BAKERY 패널을 교체해서 붙임)
        // --------------------------------------------------
        m_contentContainer = new JPanel(new BorderLayout());
        backPanel.add(m_contentContainer, BorderLayout.CENTER);

        // 메뉴 타입별 분리
        List<MenuItem> beverages = allMenus.stream()
                .filter(item -> item.GetType() == MenuItem.MenuType.Beverage)
                .collect(Collectors.toList());

        List<MenuItem> desserts = allMenus.stream()
                .filter(item -> item.GetType() == MenuItem.MenuType.Dessert)
                .collect(Collectors.toList());

        // 각 카테고리 패널 생성 (내부는 JScrollPane + 가로 스크롤)
        JPanel drinkPanel = CreateCategoryPanel(beverages);
        JPanel bakeryPanel = CreateCategoryPanel(desserts);

        // 처음에는 음료 탭을 기본으로
        ShowContentPanel(drinkPanel);

        // 버튼으로 컨텐츠 전환
        drinkButton.addActionListener(e -> ShowContentPanel(drinkPanel));
        bakeryButton.addActionListener(e -> ShowContentPanel(bakeryPanel));

        // Dialog의 contentPane 설정
        setContentPane(backPanel);

        // --------------------------------------------------
        // 📺 전체 화면으로 띄우는 설정
        // --------------------------------------------------
        setUndecorated(true);   // 프레임 테두리 제거해서 오버레이 느낌
        setResizable(false);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                               .getDefaultScreenDevice();
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();
        setBounds(bounds);      // 모니터 전체 영역으로

        // pack() / setLocationRelativeTo() 필요 없음 (이미 전체 화면)
    }

    /**
     * 중앙 contentContainer에 주어진 패널을 꽂고 다시 그리도록 하는 헬퍼 메서드
     */
    private void ShowContentPanel(JPanel panel)
    {
        m_contentContainer.removeAll();
        m_contentContainer.add(panel, BorderLayout.CENTER);
        m_contentContainer.revalidate();
        m_contentContainer.repaint();
    }

    /**
     * 카테고리별(음료/베이커리) 스크롤 가능한 패널 생성
     */
    private JPanel CreateCategoryPanel(List<MenuItem> items)
    {
        // 실제 카드들이 가로로 나열되는 영역
        JPanel scrollArea = new JPanel();
        scrollArea.setBackground(new Color(210, 230, 255));
        scrollArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollArea.setLayout(new BoxLayout(scrollArea, BoxLayout.X_AXIS));

        for (MenuItem item : items)
        {
            scrollArea.add(CreateMenuCardPanel(item));
            scrollArea.add(Box.createHorizontalStrut(20));
        }

        // 가로 스크롤만 사용 (메뉴 도감은 좌우로 넘기기)
        JScrollPane scrollPane = new JScrollPane(
                scrollArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(210, 230, 255));
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        JPanel finalWrapper = new JPanel(new BorderLayout());
        finalWrapper.setBackground(new Color(210, 230, 255));
        finalWrapper.add(scrollPane, BorderLayout.CENTER);
        return finalWrapper;
    }

    /**
     * 개별 메뉴 카드 (이름 / 이미지 박스 / 레시피 영역)
     */
    private JPanel CreateMenuCardPanel(MenuItem item)
    {
        boolean isUnlocked = item.IsUnlocked();
        String name = item.GetName();

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 500));
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        card.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(isUnlocked ? name : "???", JLabel.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(18f));
        nameLabel.setBorder(new EmptyBorder(10, 10, 5, 10));

        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel imgText = new JLabel(isUnlocked ? "이미지" : "???", JLabel.CENTER);
        imgText.setFont(imgText.getFont().deriveFont(15f));

        if (isUnlocked)
        {
            imgPanel.setBackground(Color.WHITE);
        }
        else
        {
            imgPanel.setBackground(Color.LIGHT_GRAY);
        }

        imgPanel.add(imgText, BorderLayout.CENTER);

        JLabel recipeLabel = new JLabel(isUnlocked ? "레시피" : "???", JLabel.CENTER);
        recipeLabel.setBorder(new EmptyBorder(5, 10, 10, 10));

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imgPanel, BorderLayout.CENTER);
        card.add(recipeLabel, BorderLayout.SOUTH);

        return card;
    }

    public boolean ShouldReopenPause()
    {
        return m_shouldReopenPause;
    }
}
