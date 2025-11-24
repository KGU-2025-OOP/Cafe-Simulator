package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComponent;
import javax.swing.AbstractButton;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.GridBagLayout;

public class StartMenuPanel extends JPanel {

    // [삭제] 이미지로 대체하므로 텍스트 상수 제거
    // private static final String EXIT_BUTTON_TEXT = "종료하기";

    private JButton continueButton;
    private JButton newGameButton;
    private JButton exitButton;

    private Image backgroundImage;

    public StartMenuPanel(boolean hasSaveFile) {
        backgroundImage = ImageManager.getImage(ImageManager.IMG_START_BG);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = createCenterPanel(hasSaveFile);
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JPanel createCenterPanel(boolean hasSaveFile) {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setOpaque(false);

        JPanel menuBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                super.paintComponent(g);
            }
        };

        menuBox.setLayout(new BoxLayout(menuBox, BoxLayout.Y_AXIS));
        menuBox.setOpaque(false);
        menuBox.setBorder(new EmptyBorder(40, 60, 40, 60));

        // [수정] 종료 버튼도 이미지로 생성하고 스타일 적용 (true)
        // ImageManager.BTN_EXIT 상수가 ImageManager에 정의되어 있어야 합니다!
        exitButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_EXIT));
        setCenterButtonProperties(exitButton, true);

        if (hasSaveFile) {
            continueButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_CONTINUE));
            newGameButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_NEW_GAME));

            setCenterButtonProperties(continueButton, true);
            setCenterButtonProperties(newGameButton, true);

            menuBox.add(continueButton);
            menuBox.add(Box.createVerticalStrut(20));
            menuBox.add(newGameButton);
        }
        else {
            continueButton = null;
            newGameButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_START));

            setCenterButtonProperties(newGameButton, true);

            menuBox.add(newGameButton);
        }

        menuBox.add(Box.createVerticalStrut(20));
        menuBox.add(exitButton); // 이미지로 바뀐 종료 버튼 추가

        outerPanel.add(menuBox);

        return outerPanel;
    }

    private void setCenterButtonProperties(JComponent component, boolean isImageButton) {
        // 버튼 크기 (이미지 크기에 맞춰 조절하세요)
        Dimension centerButtonSize = new Dimension(250, 70);

        if (!isImageButton) {
            centerButtonSize = new Dimension(150, 40);
        }

        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        component.setMinimumSize(centerButtonSize);
        component.setPreferredSize(centerButtonSize);
        component.setMaximumSize(centerButtonSize);

        if (component instanceof JButton) {
            JButton button = (JButton) component;

            if (isImageButton) {
                // 이미지 버튼 스타일 (투명 배경, 테두리 없음)
                button.setText("");
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setOpaque(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                // 텍스트 버튼 스타일
                Font mainFont = new Font("Malgun Gothic", Font.BOLD, 14);
                Color buttonColor = new Color(59, 89, 182);
                Color buttonTextColor = Color.WHITE;

                button.setFont(mainFont);
                button.setBackground(buttonColor);
                button.setForeground(buttonTextColor);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
            }
        }
    }

    public JButton getContinueButton() {
        return continueButton;
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}