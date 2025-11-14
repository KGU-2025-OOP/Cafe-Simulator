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
import java.awt.FlowLayout;
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

    private static final Dimension CONTENT_SIZE = new Dimension(1280, 720);

    public MenuDialog(JFrame parent, List<MenuItem> allMenus)
    {
        super(parent, "메뉴 도감", true);

        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(new Color(240, 240, 240));
        backPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
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
        backwardButton.addActionListener(e ->
        {
            this.shouldReopenPause = true;
            this.dispose();
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

        this.card = new CardLayout();
        this.cardPanel = new JPanel(this.card);
        this.cardPanel.setPreferredSize(CONTENT_SIZE);
        this.cardPanel.setMaximumSize(CONTENT_SIZE);

        List<MenuItem> beverages = allMenus.stream()
                .filter(item -> item.getType() == MenuItem.MenuType.BEVERAGE)
                .collect(Collectors.toList());
        List<MenuItem> desserts = allMenus.stream()
                .filter(item -> item.getType() == MenuItem.MenuType.DESSERT)
                .collect(Collectors.toList());

        JPanel drinkPanel = this.createCategoryPanel(beverages);
        JPanel bakeryPanel = this.createCategoryPanel(desserts);

        this.cardPanel.add(drinkPanel, "DRINK");
        this.cardPanel.add(bakeryPanel, "BAKERY");

        drinkButton.addActionListener(e -> this.card.show(this.cardPanel, "DRINK"));
        bakeryButton.addActionListener(e -> this.card.show(this.cardPanel, "BAKERY"));

        JPanel mainContentWrapper = new JPanel(new GridBagLayout());
        mainContentWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainContentWrapper.add(this.cardPanel, gbc);

        backPanel.add(mainContentWrapper, BorderLayout.CENTER);

        this.add(backPanel);

        this.setUndecorated(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        this.setSize(screenWidth, screenHeight);
        this.setLocationRelativeTo(null);
    }

    private JPanel createCategoryPanel(List<MenuItem> items)
    {
        JPanel scrollArea = new JPanel();
        scrollArea.setBackground(new Color(210, 230, 255));
        scrollArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollArea.setLayout(new BoxLayout(scrollArea, BoxLayout.X_AXIS));

        for (MenuItem item : items)
        {
            scrollArea.add(this.createMenuCardPanel(item));
            scrollArea.add(Box.createHorizontalStrut(20));
        }

        JPanel scrollAreaWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        scrollAreaWrapper.setBackground(new Color(210, 230, 255));
        scrollAreaWrapper.add(scrollArea);

        JScrollPane scrollPane = new JScrollPane(
                scrollAreaWrapper,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(210, 230, 255));

        JPanel finalWrapper = new JPanel(new BorderLayout());
        finalWrapper.add(scrollPane, BorderLayout.CENTER);
        return finalWrapper;
    }

    private JPanel createMenuCardPanel(MenuItem item)
    {
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

    public boolean shouldReopenPause()
    {
        return this.shouldReopenPause;
    }
}