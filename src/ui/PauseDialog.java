package ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractButton;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

public class PauseDialog extends JDialog
{
    private static final String DIALOG_TITLE = "일시정지";
    private static final String CALENDAR_BUTTON_TEXT = "달력";
    private static final String MENU_BUTTON_TEXT = "메뉴판";
    private static final String CONTINUE_BUTTON_TEXT = "계속하기";
    private static final String GIVE_UP_BUTTON_TEXT = "포기하기 (데이터 초기화)";
    private static final String EXIT_BUTTON_TEXT = "종료하기";
    private static final Dimension BUTTON_SIZE = new Dimension(200, 40);
    private static final int BUTTON_VERTICAL_SPACING = 15;
    private static final int DIALOG_PADDING = 20;
    private static final Font MAIN_FONT = new Font("Malgun Gothic", Font.BOLD, 14);
    private static final Color BUTTON_COLOR = new Color(59, 89, 182);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color WARN_BUTTON_COLOR = new Color(220, 53, 69);

    public enum PauseResult { CONTINUE, CALENDAR, MENU, GIVE_UP, EXIT, NONE }

    private JButton calendarButton;
    private JButton menuButton;
    private JButton continueButton;
    private JButton giveUpButton;
    private JButton exitButton;
    private PauseResult result;

    public PauseDialog(JFrame parent)
    {
        super(parent, DIALOG_TITLE, true);
        this.result = PauseResult.NONE;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(
                DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));
        this.calendarButton = new JButton(CALENDAR_BUTTON_TEXT);
        this.menuButton = new JButton(MENU_BUTTON_TEXT);
        this.continueButton = new JButton(CONTINUE_BUTTON_TEXT);
        this.giveUpButton = new JButton(GIVE_UP_BUTTON_TEXT);
        this.exitButton = new JButton(EXIT_BUTTON_TEXT);
        setButtonProperties(this.calendarButton, false);
        setButtonProperties(this.menuButton, false);
        setButtonProperties(this.continueButton, false);
        setButtonProperties(this.giveUpButton, true);
        setButtonProperties(this.exitButton, true);
        addListeners();
        mainPanel.add(this.calendarButton);
        mainPanel.add(Box.createVerticalStrut(BUTTON_VERTICAL_SPACING));
        mainPanel.add(this.menuButton);
        mainPanel.add(Box.createVerticalStrut(BUTTON_VERTICAL_SPACING));
        mainPanel.add(this.continueButton);
        mainPanel.add(Box.createVerticalStrut(BUTTON_VERTICAL_SPACING));
        mainPanel.add(this.giveUpButton);
        mainPanel.add(Box.createVerticalStrut(BUTTON_VERTICAL_SPACING));
        mainPanel.add(this.exitButton);
        add(mainPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void setButtonProperties(JComponent button, boolean isWarning)
    {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMinimumSize(BUTTON_SIZE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setFont(MAIN_FONT);
        button.setForeground(BUTTON_TEXT_COLOR);
        if (isWarning) {
            button.setBackground(WARN_BUTTON_COLOR);
        } else {
            button.setBackground(BUTTON_COLOR);
        }
        button.setOpaque(true);
        if (button instanceof AbstractButton) {
            ((AbstractButton) button).setBorderPainted(false);
        }
    }

    private void addListeners()
    {
        this.calendarButton.addActionListener(e -> {
            this.result = PauseResult.CALENDAR; dispose();
        });
        this.menuButton.addActionListener(e -> {
            this.result = PauseResult.MENU; dispose();
        });
        this.continueButton.addActionListener(e -> {
            this.result = PauseResult.CONTINUE; dispose();
        });
        this.giveUpButton.addActionListener(e -> {
            this.result = PauseResult.GIVE_UP; dispose();
        });
        this.exitButton.addActionListener(e -> {
            this.result = PauseResult.EXIT; dispose();
        });
    }

    public PauseResult getResult()
    {
        return this.result;
    }
}