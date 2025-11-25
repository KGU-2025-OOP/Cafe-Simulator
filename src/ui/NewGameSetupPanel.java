package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Cursor; // 커서 추가

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

public class NewGameSetupPanel extends JPanel {

    private JTextField nameField;
    private JButton startButton;
    private Image backgroundImage;

    public NewGameSetupPanel() {
        backgroundImage = ImageManager.getImage(ImageManager.IMG_SETUP_BG);

        setLayout(new GridBagLayout());

        // 둥근 박스 패널
        JPanel boxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 둥근 배경 (반투명 흰색)
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                // 둥근 테두리
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                super.paintComponent(g);
            }
        };

        boxPanel.setPreferredSize(new Dimension(800, 400));
        boxPanel.setOpaque(false);
        boxPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 상단 타이틀
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("카페 이름을 입력하세요!");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));

        titlePanel.add(titleLabel, BorderLayout.WEST);
        boxPanel.add(titlePanel, BorderLayout.NORTH);

        // 입력창
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints ic = new GridBagConstraints();
        ic.gridx = 0;
        ic.gridy = 0;
        ic.anchor = GridBagConstraints.CENTER;

        nameField = new JTextField(35);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 24));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(new Color(255, 248, 220));
        nameField.setBorder(
                BorderFactory.createLineBorder(Color.RED, 3, true)
        );

        inputPanel.add(nameField, ic);
        boxPanel.add(inputPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        // [수정] 창업하기 버튼 디자인 변경
        startButton = new JButton("창업하기"); // 텍스트 설정

        // 1. 배경 이미지 설정
        startButton.setIcon(ImageManager.getImageIcon(ImageManager.BTN_DEFAULT));

        // 2. 텍스트 위치 설정 (이미지 중앙에 글씨 올리기)
        startButton.setHorizontalTextPosition(JButton.CENTER);
        startButton.setVerticalTextPosition(JButton.CENTER);

        // 3. 폰트 및 색상 설정 (흰색 글씨, 맑은 고딕)
        startButton.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        startButton.setForeground(Color.WHITE);

        // 4. 버튼 스타일 제거 (투명하게)
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);

        // 5. 커서 및 크기 설정
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setPreferredSize(new Dimension(150, 50)); // 이미지 크기에 맞춰 조절 가능

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