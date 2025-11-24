package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D; // 추가
import java.awt.BasicStroke; // 추가
import java.awt.RenderingHints; // 추가
import java.awt.Image;

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

        // [수정] 박스 패널을 익명 클래스로 만들어 둥근 모서리와 테두리를 직접 그립니다.
        JPanel boxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. 둥근 배경 (반투명 흰색)
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                // 2. 둥근 테두리 (검정색, 두께 2)
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                super.paintComponent(g); // 내부 컴포넌트 그리기
            }
        };

        boxPanel.setPreferredSize(new Dimension(800, 400));
        boxPanel.setOpaque(false); // 배경은 위에서 직접 그리므로 투명 처리

        // 기존의 LineBorder는 제거하고, 내부 여백(Padding)만 남깁니다.
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

        nameField = new JTextField(35);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 24));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(new Color(255, 248, 220));
        // 입력창 자체의 테두리는 유지 (빨간색)
        nameField.setBorder(
                BorderFactory.createLineBorder(Color.RED, 3, true)
        );

        inputPanel.add(nameField, ic);
        boxPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        startButton = new JButton("창업하기");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(140, 50));

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