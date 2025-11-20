package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGuidePanel extends JPanel {

    private final Runnable backAction;
    private JPanel contentContainer;

    public MenuGuidePanel(List<MenuItem> allMenuItems, Runnable backAction) {
        this.backAction = backAction;

        setLayout(new BorderLayout());
        setBackground(new Color(210, 230, 255));
        setPreferredSize(ScreenConfig.FRAME_SIZE);

        // ======= 상단 영역 =======
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setOpaque(false);
        backPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("<-");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        backButton.addActionListener(e -> {
            if (backAction != null) backAction.run();
        });
        backPanel.add(backButton, BorderLayout.WEST);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("메뉴 도감");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titlePanel.add(titleLabel);

        JPanel tabButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tabButtonPanel.setOpaque(false);

        JButton drinkButton = new JButton("음료");
        JButton bakeryButton = new JButton("베이커리");
        drinkButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        bakeryButton.setFont(new Font("SansSerif", Font.BOLD, 18));

        tabButtonPanel.add(drinkButton);
        tabButtonPanel.add(bakeryButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(tabButtonPanel, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        

        add(topPanel, BorderLayout.NORTH);

        // ======= 가운데 영역 =======
        contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(false);
        contentContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentContainer, BorderLayout.CENTER);

        // 메뉴 분리
        List<MenuItem> beverages = allMenuItems.stream()
                .filter(m -> m.getType() == MenuItem.MenuType.Beverage)
                .collect(Collectors.toList());

        List<MenuItem> desserts = allMenuItems.stream()
                .filter(m -> m.getType() == MenuItem.MenuType.Dessert)
                .collect(Collectors.toList());

        JPanel drinkPanel = createCategoryPanel(beverages);
        JPanel dessertPanel = createCategoryPanel(desserts);

        showContentPanel(drinkPanel);

        drinkButton.addActionListener(e -> showContentPanel(drinkPanel));
        bakeryButton.addActionListener(e -> showContentPanel(dessertPanel));
    }

    private void showContentPanel(JPanel panel) {
        contentContainer.removeAll();
        contentContainer.add(panel, BorderLayout.CENTER);
        contentContainer.revalidate();
        contentContainer.repaint();
    }

    private JPanel createCategoryPanel(List<MenuItem> items) {

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 25));
        inner.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (MenuItem item : items) {
            inner.add(createMenuCardPanel(item));
        }

        JScrollPane scrollPane = new JScrollPane(
                inner,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(210, 230, 255));
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createMenuCardPanel(MenuItem item) {

        boolean isUnlocked = item.isUnlocked();
        String name = item.getName();

        JPanel card = new JPanel(new BorderLayout());

        int frameW = ScreenConfig.WIDTH;
        int frameH = ScreenConfig.HEIGHT;

        int cardW = (int) (frameW * 0.23);
        int cardH = (int) (frameH * 0.72);

        Dimension cardSize = new Dimension(cardW, cardH);

        card.setPreferredSize(cardSize);
        card.setMinimumSize(cardSize);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // ===== 이름 영역 =====
        JLabel nameLabel = new JLabel(isUnlocked ? name : "???", JLabel.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setBorder(new EmptyBorder(12, 5, 8, 5));

        // ===== 이미지/회색 칸 영역 =====
        JPanel imgWrapper = new JPanel(new BorderLayout());
        imgWrapper.setOpaque(false);
        imgWrapper.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel imgPanel = new JPanel(new BorderLayout());

        // 🔥 카드 크기에 비례해서 이미지 박스도 같이 조정
        int imgW = (int) (cardW * 0.7);
        int imgH = (int) (cardH * 0.55);
        imgPanel.setPreferredSize(new Dimension(imgW, imgH));

        imgPanel.setBackground(isUnlocked ? new Color(255, 245, 200)
                                          : new Color(220, 220, 220));

        JLabel imgLabel = new JLabel(isUnlocked ? "이미지" : "???", JLabel.CENTER);
        imgLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        imgPanel.add(imgLabel, BorderLayout.CENTER);
        imgWrapper.add(imgPanel, BorderLayout.CENTER);

        // ===== 레시피 =====
        JLabel recipeLabel = new JLabel(isUnlocked ? "레시피" : "???", JLabel.CENTER);
        recipeLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        recipeLabel.setBorder(new EmptyBorder(8, 5, 10, 5));

        // 카드 구성
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imgWrapper, BorderLayout.CENTER);
        card.add(recipeLabel, BorderLayout.SOUTH);

        return card;
    }
}
