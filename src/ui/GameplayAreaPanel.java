package ui;

import javax.swing.*;
import java.awt.*;

public class GameplayAreaPanel extends JPanel {

    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn4;

    private Image backgroundImage;

    public GameplayAreaPanel() {
        backgroundImage = ImageManager.getImage(ImageManager.IMG_MAIN_BG);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.NONE;

        this.btn1 = createStyledButton("운영시작");
        this.btn2 = createStyledButton("메뉴도감");
        this.btn3 = createStyledButton("성장도그래프");
        this.btn4 = createStyledButton("통계검색");

        gbc.gridy = 0;
        this.add(this.btn1, gbc);

        gbc.gridy = 1;
        this.add(this.btn2, gbc);

        gbc.gridy = 2;
        this.add(this.btn3, gbc);
        
        gbc.gridy = 3;
        this.add(this.btn4, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setIcon(ImageManager.getImageIcon(ImageManager.BTN_DEFAULT));

        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);

        button.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        button.setForeground(Color.WHITE);

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.setPreferredSize(new Dimension(200, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getBtn1() {
        return this.btn1;
    }

    public JButton getBtn2() {
        return this.btn2;
    }

    public JButton getBtn3() {
        return this.btn3;
    }
    
    public JButton getBtn4() {
    	return this.btn4; 
    }
}