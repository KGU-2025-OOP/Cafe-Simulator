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
        // ScreenConfig가 없다면 아래 줄을 주석 처리하고 setPreferredSize(new Dimension(1280, 720)); 으로 바꾸세요.
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

        JButton coffeeButton = new JButton("커피");
        JButton nonCoffeeButton = new JButton("논커피");

        coffeeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        nonCoffeeButton.setFont(new Font("SansSerif", Font.BOLD, 18));

        tabButtonPanel.add(coffeeButton);
        tabButtonPanel.add(nonCoffeeButton);

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

        List<MenuItem> coffeeList = allMenuItems.stream()
                .filter(m -> m.getType() == MenuItem.MenuType.Coffee)
                .collect(Collectors.toList());

        List<MenuItem> nonCoffeeList = allMenuItems.stream()
                .filter(m -> m.getType() == MenuItem.MenuType.NonCoffee)
                .collect(Collectors.toList());

        JPanel coffeePanel = createCategoryPanel(coffeeList);
        JPanel nonCoffeePanel = createCategoryPanel(nonCoffeeList);

        // 기본 화면
        showContentPanel(coffeePanel);

        // 탭 전환 리스너
        coffeeButton.addActionListener(e -> showContentPanel(coffeePanel));
        nonCoffeeButton.addActionListener(e -> showContentPanel(nonCoffeePanel));
    }

    // 탭 전환 시 패널 교체 및 스크롤 초기화
    private void showContentPanel(JPanel panel) {
        contentContainer.removeAll();
        contentContainer.add(panel, BorderLayout.CENTER);

        // 스크롤 위치 초기화 (맨 왼쪽/맨 위로)
        for (Component c : panel.getComponents()) {
            if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                SwingUtilities.invokeLater(() -> {
                    sp.getHorizontalScrollBar().setValue(0);
                    sp.getVerticalScrollBar().setValue(0);
                });
                break;
            }
        }

        contentContainer.revalidate();
        contentContainer.repaint();
    }

    private JPanel createCategoryPanel(List<MenuItem> items) {
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 25));
        inner.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("해당 카테고리의 메뉴가 없습니다.");
            emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            inner.add(emptyLabel);
        } else {
            for (MenuItem item : items) {
                inner.add(createMenuCardPanel(item));
            }
        }

        JScrollPane scrollPane = new JScrollPane(
                inner,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(210, 230, 255));
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createMenuCardPanel(MenuItem item) {
        boolean isUnlocked = item.isUnlocked();
        String name = item.getName();
        int price = item.getPrice(); // 가격 정보 가져오기

        JPanel card = new JPanel(new BorderLayout());

        int frameW = 1280; // ScreenConfig.WIDTH 대신 직접 숫자 사용 (오류 방지용)
        int frameH = 720;

        int cardW = (int) (frameW * 0.23);
        int cardH = (int) (frameH * 0.72);

        Dimension cardSize = new Dimension(cardW, cardH);

        card.setPreferredSize(cardSize);
        card.setMinimumSize(cardSize);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // ===== 상단 헤더 (이름 + 가격) =====
        JPanel headerPanel = new JPanel(new GridLayout(2, 1)); // 2줄 (이름, 가격)
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(12, 5, 5, 5)); // 전체 여백

        // 1. 이름 라벨
        JLabel nameLabel = new JLabel(isUnlocked ? name : "???", JLabel.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // 2. 가격 라벨 (작게 표시)
        String priceText = isUnlocked ? String.format("%,d원", price) : "-";
        JLabel priceLabel = new JLabel(priceText, JLabel.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14)); // 이름보다 작게
        priceLabel.setForeground(Color.DARK_GRAY); // 회색으로

        headerPanel.add(nameLabel);
        headerPanel.add(priceLabel);

        // ===== 이미지 영역 =====
        JPanel imgWrapper = new JPanel(new BorderLayout());
        imgWrapper.setOpaque(false);
        imgWrapper.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel imgPanel = new JPanel(new BorderLayout());

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
        String recipeText = "???";
        if (isUnlocked && item.getIngredients() != null && !item.getIngredients().isEmpty()) {
            recipeText = String.join(", ", item.getIngredients());
        }

        JLabel recipeLabel = new JLabel(recipeText, JLabel.CENTER);
        recipeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        recipeLabel.setBorder(new EmptyBorder(8, 5, 10, 5));

        // 카드에 조립
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(imgWrapper, BorderLayout.CENTER);
        card.add(recipeLabel, BorderLayout.SOUTH);

        return card;
    }
}