package ui;

import javax.swing.*;
import java.awt.*;

public class GameplayAreaPanel extends JPanel {

    private JButton btn1;
    private JButton btn2;
    private JButton btn3;

    private Image backgroundImage;

    public GameplayAreaPanel() {
        // 1. 배경 이미지 로드
        backgroundImage = ImageManager.getImage(ImageManager.IMG_MAIN_BG);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(15, 10, 15, 10); // 간격을 조금 넓힘
        gbc.fill = GridBagConstraints.NONE; // 버튼 크기 고정을 위해 NONE으로 변경

        // [수정] 버튼 생성 (스타일 적용)
        this.btn1 = createStyledButton("운영시작");
        this.btn2 = createStyledButton("메뉴도감");
        this.btn3 = createStyledButton("성장도그래프");

        gbc.gridy = 0;
        this.add(this.btn1, gbc);

        gbc.gridy = 1;
        this.add(this.btn2, gbc);

        gbc.gridy = 2;
        this.add(this.btn3, gbc);
    }

    // [추가] 버튼 스타일을 설정하는 헬퍼 메서드
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        // 1. 배경 이미지 설정 (default_button.png)
        button.setIcon(ImageManager.getImageIcon(ImageManager.BTN_DEFAULT));

        // 2. 텍스트 위치 설정 (이미지 중앙)
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);

        // 3. 폰트 및 색상 (흰색, 맑은 고딕 Bold)
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        button.setForeground(Color.WHITE);

        // 4. 버튼 투명화 (이미지만 보이게)
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        // 5. 크기 및 커서 설정
        // 이미지 크기에 맞춰 적절히 조절 (예: 200x60)
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
}