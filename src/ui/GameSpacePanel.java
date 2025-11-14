package ui;

import javax.swing.*;
import java.awt.*;

public class GameSpacePanel extends JPanel
{
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;

    public GameSpacePanel()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font buttonFont = new Font("Malgun Gothic", Font.BOLD, 18);
        Dimension buttonSize = new Dimension(200, 60);

        this.btn1 = new JButton("운영시작");
        this.btn2 = new JButton("메뉴도감");
        this.btn3 = new JButton("성장도그래프");

        this.btn1.setFont(buttonFont);
        this.btn2.setFont(buttonFont);
        this.btn3.setFont(buttonFont);

        this.btn1.setPreferredSize(buttonSize);
        this.btn2.setPreferredSize(buttonSize);
        this.btn3.setPreferredSize(buttonSize);

        gbc.gridy = 0;
        this.add(this.btn1, gbc);
        gbc.gridy = 1;
        this.add(this.btn2, gbc);
        gbc.gridy = 2;
        this.add(this.btn3, gbc);
    }

    public JButton getBtn1()
    {
        return this.btn1;
    }

    public JButton getBtn2()
    {
        return this.btn2;
    }

    public JButton getBtn3()
    {
        return this.btn3;
    }
}