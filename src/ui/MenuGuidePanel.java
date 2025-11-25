package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionListener;

public class MenuGuidePanel extends JPanel {

    private final Runnable backAction;
    private JPanel contentContainer;
    private Image backgroundImage;

    public MenuGuidePanel(List<MenuItem> allMenuItems, Runnable backAction) {
        this.backAction = backAction;

        backgroundImage = ImageManager.getImage(ImageManager.IMG_MENU_BG);

        setLayout(new BorderLayout());
        setPreferredSize(ScreenConfig.FRAME_SIZE);

        // ======= 상단 영역 (GridBagLayout) =======
        JPanel backPanel = new JPanel(new GridBagLayout());
        backPanel.setOpaque(false);
        backPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton backButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_BACK));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            if (backAction != null) backAction.run();
        });
        backPanel.add(backButton);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        // [수정] 타이틀 라벨 꾸미기 (그림자 효과 추가)
        JLabel titleLabel = new JLabel("메뉴 도감") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                // 텍스트 부드럽게 처리 (안티앨리어싱)
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                String text = getText();
                FontMetrics fm = g2.getFontMetrics();

                // 중앙 정렬 좌표 계산
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                // 1. 그림자 그리기 (검은색 반투명, 약간 오른쪽 아래로)
                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawString(text, x + 3, y + 3);

                // 2. 메인 텍스트 그리기 (흰색)
                g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);
            }
        };

        // 폰트 설정 (맑은 고딕, 굵게, 크기 40)
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        // 라벨 크기 넉넉하게 지정 (글자가 잘리지 않도록)
        titleLabel.setPreferredSize(new Dimension(250, 60));

        titlePanel.add(titleLabel);


        JPanel tabButtonPanel = new JPanel(new GridBagLayout());
        tabButtonPanel.setOpaque(false);

        JButton coffeeButton = createToggleButton("커피");
        JButton nonCoffeeButton = createToggleButton("논커피");

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

    // 토글 버튼 스타일 헬퍼
    private JButton createToggleButton(String text) {
        JButton btn = new JButton(text);
        btn.setIcon(ImageManager.getImageIcon(ImageManager.BTN_MENU_TOGGLE)); // 이미지가 있다면 적용됨
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

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
            emptyLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
            emptyLabel.setForeground(Color.WHITE); // 배경이 어두울 수 있으니 흰색
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

        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUI(new CafeScrollBarUI());

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
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

        // ===== 헤더 =====
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(12, 5, 5, 5));

        JLabel nameLabel = new JLabel(isUnlocked ? name : "???", JLabel.CENTER);
        nameLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20)); // 폰트 통일

        String priceText = isUnlocked ? String.format("%,d원", price) : "-";
        JLabel priceLabel = new JLabel(priceText, JLabel.CENTER);
        priceLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        priceLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(nameLabel);
        headerPanel.add(priceLabel);

        // ===== 이미지 =====
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
            imgLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
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
        recipeLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        recipeLabel.setBorder(new EmptyBorder(8, 5, 10, 5));

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(imgWrapper, BorderLayout.CENTER);
        card.add(recipeLabel, BorderLayout.SOUTH);

        return card;
    }

    private static class CafeScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(160, 110, 80));
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4,
                    10, 10);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }
    }
}