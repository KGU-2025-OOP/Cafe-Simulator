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
    private static final int BUTTON_VERTICAL_SPACING = 20;
    private static final Dimension CENTER_BUTTON_SIZE = new Dimension(150, 40);
    private static final int PANEL_PADDING = 10;
    private static final Font MAIN_FONT = new Font("Malgun Gothic", Font.BOLD, 14);
    private static final Color BUTTON_COLOR = new Color(59, 89, 182);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Font SMALL_FONT = new Font("Malgun Gothic", Font.PLAIN, 10);

    private JButton continueButton;
    private JButton newGameButton;
    private JButton exitButton;
    private JToggleButton bgmButton;

    public StartPanel(boolean hasSaveFile)
    {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
        this.bgmButton = new JToggleButton(BGM_BUTTON_ON_TEXT, true);
        this.bgmButton.setFont(SMALL_FONT);
        JPanel centerPanel = createCenterPanel(hasSaveFile);
        JPanel bottomPanel = createBottomPanel();
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        addBgmButtonListener();
    }

    private JPanel createCenterPanel(boolean hasSaveFile)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.exitButton = new JButton(EXIT_BUTTON_TEXT);
        setCenterButtonProperties(this.exitButton);
        panel.add(Box.createVerticalGlue());
        if (hasSaveFile) {
            this.continueButton = new JButton(CONTINUE_BUTTON_TEXT);
            this.newGameButton = new JButton(NEW_GAME_BUTTON_TEXT);
            setCenterButtonProperties(this.continueButton);
            setCenterButtonProperties(this.newGameButton);
            panel.add(this.continueButton);
            panel.add(Box.createVerticalStrut(BUTTON_VERTICAL_SPACING));
            panel.add(this.newGameButton);
        } else {
            this.continueButton = null;
            this.newGameButton = new JButton(START_BUTTON_TEXT);
            setCenterButtonProperties(this.newGameButton);
            panel.add(this.newGameButton);
        }
        panel.add(Box.createVerticalStrut(BUTTON_VERTICAL_SPACING));
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
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMinimumSize(CENTER_BUTTON_SIZE);
        button.setPreferredSize(CENTER_BUTTON_SIZE);
        button.setMaximumSize(CENTER_BUTTON_SIZE);
        button.setFont(MAIN_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setOpaque(true);
        if (button instanceof AbstractButton) {
            ((AbstractButton) button).setBorderPainted(false);
        }
    }

    private void addBgmButtonListener()
    {
        this.bgmButton.addActionListener(e -> {
            if (this.bgmButton.isSelected()) {
                this.bgmButton.setText(BGM_BUTTON_ON_TEXT);
            } else {
                this.bgmButton.setText(BGM_BUTTON_OFF_TEXT);
            }
        });
    }

    public JButton getContinueButton() { return this.continueButton; }
    public JButton getNewGameButton() { return this.newGameButton; }
    public JButton getExitButton() { return this.exitButton; }
    public JToggleButton getBgmButton() { return this.bgmButton; }
}