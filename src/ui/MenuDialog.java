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
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout; // [추가]
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

public class MenuDialog extends JDialog
{
    private CardLayout card;
    private JPanel cardPanel;
    private boolean shouldReopenPause = false;

    // [신규] 메뉴도감 컨텐츠의 고정 크기
    private static final Dimension CONTENT_SIZE = new Dimension(1280, 720);

    public MenuDialog(JFrame parent, List<MenuItem> allMenus)
    {
        super(parent, "메뉴 도감", true);

        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(new Color(240, 240, 240)); // 배경색 지정
        backPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // 상단 패널 (뒤로가기, 제목, 탭 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // backPanel 배경색 따름
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JPanel backwardPanel = new JPanel(new BorderLayout());
        backwardPanel.setOpaque(false);
        backwardPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JButton backwardButton = new JButton("<-");
        backwardButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        backwardButton.addActionListener(e -> {
            this.shouldReopenPause = true;
            dispose();
        });

        backwardPanel.add(backwardButton);
        topPanel.add(backwardPanel, BorderLayout.WEST);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("메뉴 도감");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setOpaque(false);
        JButton drinkButton = new JButton("음료");
        JButton bakeryButton = new JButton("베이커리");

        buttonPanel.add(drinkButton);
        buttonPanel.add(bakeryButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        backPanel.add(topPanel, BorderLayout.NORTH);

        // 가운데 CardLayout
        card = new CardLayout();
        cardPanel = new JPanel(card);
        // [수정] cardPanel의 크기를 고정 (비율 유지)
        cardPanel.setPreferredSize(CONTENT_SIZE);
        cardPanel.setMaximumSize(CONTENT_SIZE);

        // GameFrame 데이터로 UI 생성
        List<MenuItem> beverages = allMenus.stream()
                .filter(item -> item.getType() == MenuItem.MenuType.BEVERAGE)
                .collect(Collectors.toList());
        List<MenuItem> desserts = allMenus.stream()
                .filter(item -> item.getType() == MenuItem.MenuType.DESSERT)
                .collect(Collectors.toList());

        JPanel drinkPanel = createCategoryPanel(beverages);
        JPanel bakeryPanel = createCategoryPanel(desserts);

        cardPanel.add(drinkPanel, "DRINK");
        cardPanel.add(bakeryPanel, "BAKERY");

        drinkButton.addActionListener(e -> card.show(cardPanel, "DRINK"));
        bakeryButton.addActionListener(e -> card.show(cardPanel, "BAKERY"));

        // [신규] cardPanel을 중앙에 배치할 래퍼 패널 생성
        JPanel mainContentWrapper = new JPanel(new GridBagLayout());
        mainContentWrapper.setOpaque(false); // backPanel 배경색 따름
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬
        gbc.fill = GridBagConstraints.NONE; // 크기 고정
        mainContentWrapper.add(cardPanel, gbc);

        // [수정] 래퍼 패널을 CENTER에 추가
        backPanel.add(mainContentWrapper, BorderLayout.CENTER);

        add(backPanel);

        // --- 🔽 JDialog용 전체 화면 설정 🔽 ---
        setUndecorated(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(null);
        // --- 🔼 JDialog용 전체 화면 설정 🔼 ---
    }

    private JPanel createCategoryPanel(List<MenuItem> items) {
        // [수정] wrapper 패널 제거 (불필요)

        JPanel scrollArea = new JPanel();
        scrollArea.setBackground(new Color(210, 230, 255));
        scrollArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollArea.setLayout(new BoxLayout(scrollArea, BoxLayout.X_AXIS));

        for (MenuItem item : items) {
            scrollArea.add(createMenuCardPanel(item));
            scrollArea.add(Box.createHorizontalStrut(20));
        }

        // [신규] BoxLayout(X_AXIS)가 세로로 늘어나는 것을 방지하기 위해 FlowLayout 패널로 감쌈
        JPanel scrollAreaWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        scrollAreaWrapper.setBackground(new Color(210, 230, 255));
        scrollAreaWrapper.add(scrollArea);

        JScrollPane scrollPane = new JScrollPane (
                scrollAreaWrapper, // [수정] scrollArea -> scrollAreaWrapper
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(210, 230, 255)); // 스크롤바 배경 통일

        // [신규] JScrollPane이 CardLayout에서 늘어날 수 있도록 BorderLayout 패널에 담아 반환
        JPanel finalWrapper = new JPanel(new BorderLayout());
        finalWrapper.add(scrollPane, BorderLayout.CENTER);
        return finalWrapper;
    }

    private JPanel createMenuCardPanel(MenuItem item) {
        boolean isUnlocked = item.isUnlocked();
        String name = item.getName();

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

        if (isUnlocked) {
            imgPanel.setBackground(Color.WHITE);
        }
        else {
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

    public boolean shouldReopenPause() {
        return this.shouldReopenPause;
    }
}