package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JComponent;
import javax.swing.AbstractButton;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;

public class StartPanel extends JPanel
{
    private static final String CONTINUE_BUTTON_TEXT = "계속하기";
    private static final String NEW_GAME_BUTTON_TEXT = "새로하기";
    private static final String START_BUTTON_TEXT = "시작";
    private static final String EXIT_BUTTON_TEXT = "종료하기";
    private static final String BGM_BUTTON_ON_TEXT = "BGM ON";
    private static final String BGM_BUTTON_OFF_TEXT = "BGM OFF";

    private JButton continueButton;
    private JButton newGameButton;
    private JButton exitButton;
    private JToggleButton bgmButton;

    public StartPanel(boolean hasSaveFile)
    {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.bgmButton = new JToggleButton(BGM_BUTTON_ON_TEXT, true);
        this.bgmButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 10));
        JPanel centerPanel = this.createCenterPanel(hasSaveFile);
        JPanel bottomPanel = this.createBottomPanel();
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.addBgmButtonListener();
    }

    private JPanel createCenterPanel(boolean hasSaveFile)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.exitButton = new JButton(EXIT_BUTTON_TEXT);
        this.setCenterButtonProperties(this.exitButton);
        panel.add(Box.createVerticalGlue());
        if (hasSaveFile)
        {
            this.continueButton = new JButton(CONTINUE_BUTTON_TEXT);
            this.newGameButton = new JButton(NEW_GAME_BUTTON_TEXT);
            this.setCenterButtonProperties(this.continueButton);
            this.setCenterButtonProperties(this.newGameButton);
            panel.add(this.continueButton);
            panel.add(Box.createVerticalStrut(20));
            panel.add(this.newGameButton);
        }
        else
        {
            this.continueButton = null;
            this.newGameButton = new JButton(START_BUTTON_TEXT);
            this.setCenterButtonProperties(this.newGameButton);
            panel.add(this.newGameButton);
        }
        panel.add(Box.createVerticalStrut(20));
        panel.add(this.exitButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createBottomPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(this.bgmButton);
        return panel;
    }

    private void setCenterButtonProperties(JComponent button)
    {
        Dimension centerButtonSize = new Dimension(150, 40);
        Font mainFont = new Font("Malgun Gothic", Font.BOLD, 14);
        Color buttonColor = new Color(59, 89, 182);
        Color buttonTextColor = Color.WHITE;

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMinimumSize(centerButtonSize);
        button.setPreferredSize(centerButtonSize);
        button.setMaximumSize(centerButtonSize);
        button.setFont(mainFont);
        button.setBackground(buttonColor);
        button.setForeground(buttonTextColor);
        button.setOpaque(true);
        if (button instanceof AbstractButton)
        {
            ((AbstractButton) button).setBorderPainted(false);
        }
    }

    private void addBgmButtonListener()
    {
        this.bgmButton.addActionListener(e ->
        {
            if (this.bgmButton.isSelected())
            {
                this.bgmButton.setText(BGM_BUTTON_ON_TEXT);
            }
            else
            {
                this.bgmButton.setText(BGM_BUTTON_OFF_TEXT);
            }
        });
    }

    public JButton getContinueButton()
    {
        return this.continueButton;
    }

    public JButton getNewGameButton()
    {
        return this.newGameButton;
    }

    public JButton getExitButton()
    {
        return this.exitButton;
    }

    public JToggleButton getBgmButton()
    {
        return this.bgmButton;
    }
}