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

public class StartPanel extends JPanel
{
    private static final String CONTINUE_BUTTON_TEXT = "계속하기";
    private static final String NEW_GAME_BUTTON_TEXT = "새로하기";
    private static final String START_BUTTON_TEXT = "시작";
    private static final String EXIT_BUTTON_TEXT = "종료하기";

    private JButton m_continueButton;
    private JButton m_newGameButton;
    private JButton m_exitButton;

    public StartPanel(boolean hasSaveFile)
    {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = CreateCenterPanel(hasSaveFile);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel CreateCenterPanel(boolean hasSaveFile)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        m_exitButton = new JButton(EXIT_BUTTON_TEXT);
        SetCenterButtonProperties(m_exitButton);
        panel.add(Box.createVerticalGlue());
        if (hasSaveFile)
        {
            m_continueButton = new JButton(CONTINUE_BUTTON_TEXT);
            m_newGameButton = new JButton(NEW_GAME_BUTTON_TEXT);
            SetCenterButtonProperties(m_continueButton);
            SetCenterButtonProperties(m_newGameButton);
            panel.add(m_continueButton);
            panel.add(Box.createVerticalStrut(20));
            panel.add(m_newGameButton);
        }
        else
        {
            m_continueButton = null;
            m_newGameButton = new JButton(START_BUTTON_TEXT);
            SetCenterButtonProperties(m_newGameButton);
            panel.add(m_newGameButton);
        }
        panel.add(Box.createVerticalStrut(20));
        panel.add(m_exitButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private void SetCenterButtonProperties(JComponent button)
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

    public JButton getContinueButton()
    {
        return m_continueButton;
    }

    public JButton getNewGameButton()
    {
        return m_newGameButton;
    }

    public JButton getExitButton()
    {
        return m_exitButton;
    }

}