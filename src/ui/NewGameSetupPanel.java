package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGameSetupPanel extends JPanel {
	
	private JTextField nameField;
    private JButton startButton;

    public NewGameSetupPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel boxPanel = new JPanel(new BorderLayout());
        boxPanel.setPreferredSize(new Dimension(800, 400));
        boxPanel.setBackground(new Color(250, 250, 250));
        boxPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(30, 40, 30, 40)
                ));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("카페 이름을 입력하세요!");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));

        titlePanel.add(titleLabel, BorderLayout.WEST);
        boxPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints ic = new GridBagConstraints();
        ic.gridx = 0;
        ic.gridy = 0;
        ic.anchor = GridBagConstraints.CENTER;

        nameField = new JTextField(35);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 24));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(new Color(255, 248, 220));
        nameField.setBorder(
                BorderFactory.createLineBorder(Color.RED, 3, true)
        );

        inputPanel.add(nameField, ic);
        boxPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        startButton = new JButton("창업하기");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(140, 50));

        buttonPanel.add(startButton, BorderLayout.EAST);
        boxPanel.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        add(boxPanel, gbc);
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JButton getStartButton() {
        return startButton;
    }
}