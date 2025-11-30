package ui;

import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGameSetupPanel extends JPanel {

    private JTextField nameField;
    private JButton startButton;
    private Image backgroundImage;

    public NewGameSetupPanel() {
        backgroundImage = ImageManager.getImage(ImageManager.IMG_SETUP_BG);

        setLayout(new GridBagLayout());

        JPanel boxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

                super.paintComponent(g);
            }
        };

        boxPanel.setPreferredSize(new Dimension(800, 400));
        boxPanel.setOpaque(false);
        boxPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("카페 이름을 입력하세요!");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));

        titlePanel.add(titleLabel, BorderLayout.WEST);
        boxPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints ic = new GridBagConstraints();
        ic.gridx = 0;
        ic.gridy = 0;
        ic.anchor = GridBagConstraints.CENTER;

        // [추가된 부분] ---------------------------------
        ic.weightx = 1.0; // 가로 공간에 대한 가중치 부여 (이게 없으면 최소 크기로 줄어듦)
        ic.fill = GridBagConstraints.HORIZONTAL; // 할당된 가로 공간을 꽉 채우도록 설정
        // ---------------------------------------------

        nameField = new JTextField(35);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 24));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(new Color(255, 248, 220));
        nameField.setBorder(
                BorderFactory.createLineBorder(new Color(129, 0, 26), 3, true)
        );

        inputPanel.add(nameField, ic);
        boxPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        startButton = new JButton("창업하기");

        startButton.setIcon(ImageManager.getImageIcon(ImageManager.BTN_DEFAULT));
        startButton.setHorizontalTextPosition(JButton.CENTER);
        startButton.setVerticalTextPosition(JButton.CENTER);

        startButton.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        startButton.setForeground(Color.WHITE);

        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.setPreferredSize(new Dimension(120, 45));

        nameField.addActionListener(e -> startButton.doClick());

        buttonPanel.add(startButton, BorderLayout.EAST);
        boxPanel.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);

        add(boxPanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JTextField getNameField() {
        return nameField;
    }



    public JButton getStartButton() {
        return startButton;
    }
}