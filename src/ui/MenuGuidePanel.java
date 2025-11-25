package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGuidePanel extends JPanel {

    private final Runnable backAction;
    private JPanel contentContainer;

    // [추가] 배경 이미지 변수
    private Image backgroundImage;

    public MenuGuidePanel(List<MenuItem> allMenuItems, Runnable backAction) {
        this.backAction = backAction;

        // [추가] 배경 이미지 로드
        backgroundImage = ImageManager.getImage(ImageManager.IMG_MENU_BG);

        setLayout(new BorderLayout());
        // [삭제] 배경색 설정 제거 (이미지가 대신함)
        // setBackground(new Color(210, 230, 255));
        setPreferredSize(ScreenConfig.FRAME_SIZE);

        // ======= 상단 영역 (GridBagLayout 사용) =======
        JPanel backPanel = new JPanel(new GridBagLayout());
        backPanel.setOpaque(false);
        backPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton backButton = new JButton("<-");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        backButton.setMargin(new Insets(0, 10, 0, 10));
        backButton.addActionListener(e -> {
            if (backAction != null) backAction.run();
        });
        backPanel.add(backButton);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("메뉴 도감");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titlePanel.add(titleLabel);

        JPanel tabButtonPanel = new JPanel(new GridBagLayout());
        tabButtonPanel.setOpaque(false);

        JButton coffeeButton = new JButton("커피");
        JButton nonCoffeeButton = new JButton("논커피");

        coffeeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        nonCoffeeButton.setFont(new Font("SansSerif", Font.BOLD, 18));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        tabButtonPanel.add(coffeeButton, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        tabButtonPanel.add(nonCoffeeButton, gbc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(tabButtonPanel, BorderLayout.EAST);

        topPanel.setBorder(new EmptyBorder(20, 30, 0, 30));

        add(topPanel, BorderLayout.NORTH);

        // ======= 가운데 영역 =======
        contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(false);
        contentContainer.setBorder(new EmptyBorder(0, 10, 10, 10));
        add(contentContainer, BorderLayout.CENTER);

        List<MenuItem> coffeeList = allMenuItems.stream()
                .filter(m -> m.getType() == MenuItem.MenuType.Coffee)
                .collect(Collectors.toList());

        List<MenuItem> nonCoffeeList = allMenuItems.stream()
                .filter(m -> m.getType() == MenuItem.MenuType.NonCoffee)
                .collect(Collectors.toList());

        JPanel coffeePanel = createCategoryPanel(coffeeList);
        JPanel nonCoffeePanel = createCategoryPanel(nonCoffeeList);

        showContentPanel(coffeePanel);

        coffeeButton.addActionListener(e -> showContentPanel(coffeePanel));
        nonCoffeeButton.addActionListener(e -> showContentPanel(nonCoffeePanel));
    }

    // [추가] 배경 이미지 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ... (이하 showContentPanel, createCategoryPanel, createMenuCardPanel 메서드는 기존과 동일) ...
    private void showContentPanel(JPanel panel) {
        contentContainer.removeAll();
        contentContainer.add(panel, BorderLayout.CENTER);

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
        // [수정] 래퍼 패널도 투명하게 설정 (배경 이미지 보이게)
        wrapper.setOpaque(false);
        // wrapper.setBackground(new Color(210, 230, 255)); // 삭제
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createMenuCardPanel(MenuItem item) {
        boolean isUnlocked = item.isUnlocked();
        String name = item.getName();
        int price = item.getPrice();

        JPanel card = new JPanel(new BorderLayout());

        int frameW = 1280;
        int frameH = 720;

        int cardW = (int) (frameW * 0.23);
        int cardH = (int) (frameH * 0.72);

        Dimension cardSize = new Dimension(cardW, cardH);

        card.setPreferredSize(cardSize);
        card.setMinimumSize(cardSize);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // ===== 상단 헤더 =====
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(12, 5, 5, 5));

        JLabel nameLabel = new JLabel(isUnlocked ? name : "???", JLabel.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        String priceText = isUnlocked ? String.format("%,d원", price) : "-";
        JLabel priceLabel = new JLabel(priceText, JLabel.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(nameLabel);
        headerPanel.add(priceLabel);

        // ===== 이미지 영역 =====
        JPanel imgWrapper = new JPanel(new BorderLayout());
        imgWrapper.setOpaque(false);
        imgWrapper.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel imgLabel = new JLabel("", JLabel.CENTER);
        int imgW = (int) (cardW * 0.8);
        int imgH = (int) (cardH * 0.5);
        imgLabel.setPreferredSize(new Dimension(imgW, imgH));

        if (isUnlocked) {
            String imagePath = "/image/menu/" + item.getImagePath();
            ImageIcon originalIcon = ImageManager.getImageIcon(imagePath);
            if (originalIcon != null && originalIcon.getIconWidth() > 0) {
                Image scaledImg = originalIcon.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                imgLabel.setText("이미지 없음");
            }
        } else {
            imgLabel.setText("???");
            imgLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
            imgLabel.setOpaque(true);
            imgLabel.setBackground(new Color(220, 220, 220));
        }

        imgWrapper.add(imgLabel, BorderLayout.CENTER);

        // ===== 레시피 =====
        String recipeText = "???";
        if (isUnlocked && item.getIngredients() != null && !item.getIngredients().isEmpty()) {
            recipeText = String.join(", ", item.getIngredients());
        }

        JLabel recipeLabel = new JLabel(recipeText, JLabel.CENTER);
        recipeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        recipeLabel.setBorder(new EmptyBorder(8, 5, 10, 5));

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(imgWrapper, BorderLayout.CENTER);
        card.add(recipeLabel, BorderLayout.SOUTH);

        return card;
    }
}