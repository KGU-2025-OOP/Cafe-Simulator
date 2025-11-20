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

public class PauseMenuDialog extends JDialog {
	
    private static final String DIALOG_TITLE = "일시정지";
    private static final String CALENDAR_BUTTON_TEXT = "달력";
    private static final String MENU_BUTTON_TEXT = "메뉴판";
    private static final String CONTINUE_BUTTON_TEXT = "계속하기";
    private static final String GIVE_UP_BUTTON_TEXT = "포기하기 (데이터 초기화)";
    private static final String EXIT_BUTTON_TEXT = "종료하기";

    public enum PauseResult {
        CONTINUE,
        MENU,
        GIVE_UP,
        EXIT,
        NONE
    }

    private JButton menuButton;
    private JButton continueButton;
    private JButton giveUpButton;
    private JButton exitButton;
    private PauseResult result;

    public PauseMenuDialog(JFrame parent) {
        super(parent, DIALOG_TITLE, true);
        this.result = PauseResult.NONE;
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.menuButton = new JButton(MENU_BUTTON_TEXT);
        this.continueButton = new JButton(CONTINUE_BUTTON_TEXT);
        this.giveUpButton = new JButton(GIVE_UP_BUTTON_TEXT);
        this.exitButton = new JButton(EXIT_BUTTON_TEXT);

        this.setButtonProperties(this.menuButton, false);
        this.setButtonProperties(this.continueButton, false);
        this.setButtonProperties(this.giveUpButton, true);
        this.setButtonProperties(this.exitButton, true);
        this.addListeners();

        mainPanel.add(this.menuButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(this.continueButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(this.giveUpButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(this.exitButton);

        this.add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void setButtonProperties(JComponent button, boolean isWarning) {
        Dimension buttonSize = new Dimension(200, 40);
        Font mainFont = new Font("Malgun Gothic", Font.BOLD, 14);
        Color buttonColor = new Color(59, 89, 182);
        Color buttonTextColor = Color.WHITE;
        Color warnButtonColor = new Color(220, 53, 69);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMinimumSize(buttonSize);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setFont(mainFont);
        button.setForeground(buttonTextColor);
        
        if (isWarning) {
            button.setBackground(warnButtonColor);
        }
        else {
            button.setBackground(buttonColor);
        }
        button.setOpaque(true);
        
        if (button instanceof AbstractButton) {
            ((AbstractButton) button).setBorderPainted(false);
        }
    }

    private void addListeners() {        
        this.menuButton.addActionListener(e -> {
            this.result = PauseResult.MENU;
            this.dispose();
        });
        
        this.continueButton.addActionListener(e -> {
            this.result = PauseResult.CONTINUE;
            this.dispose();
        });
        
        this.giveUpButton.addActionListener(e -> {
            this.result = PauseResult.GIVE_UP;
            this.dispose();
        });
        
        this.exitButton.addActionListener(e -> {
            this.result = PauseResult.EXIT;
            this.dispose();
        });
    }

    public PauseResult getResult() {
        return this.result;
    }
}