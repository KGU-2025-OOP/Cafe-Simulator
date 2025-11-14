package ui;

import javax.swing.*;
import java.awt.*;

/**
 * 게임의 메인 허브 역할을 하는 패널 (운영시작, 메뉴도감, 성장도그래프)
 */
public class GameSpacePanel extends JPanel {

    private JButton btn1; // 운영시작
    private JButton btn2; // 메뉴도감
    private JButton btn3; // 성장도그래프

    public GameSpacePanel() {

        // 버튼들이 화면 중앙에 모이도록 GridBagLayout 사용
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 사이 여백
        gbc.fill = GridBagConstraints.HORIZONTAL; // 버튼 크기 동일하게

        Font buttonFont = new Font("Malgun Gothic", Font.BOLD, 18);
        Dimension buttonSize = new Dimension(200, 60);

        // 버튼 생성
        btn1 = new JButton("운영시작");
        btn2 = new JButton("메뉴도감");
        btn3 = new JButton("성장도그래프");

        // 버튼 스타일 설정
        btn1.setFont(buttonFont);
        btn2.setFont(buttonFont);
        btn3.setFont(buttonFont);

        btn1.setPreferredSize(buttonSize);
        btn2.setPreferredSize(buttonSize);
        btn3.setPreferredSize(buttonSize);

        // 패널에 버튼 추가 (세로로 정렬)
        gbc.gridy = 0;
        add(btn1, gbc);
        gbc.gridy = 1;
        add(btn2, gbc);
        gbc.gridy = 2;
        add(btn3, gbc);
    }

    // GameFrame에서 버튼에 접근할 수 있도록 getter 제공
    public JButton getBtn1() { return btn1; }
    public JButton getBtn2() { return btn2; }
    public JButton getBtn3() { return btn3; }
}