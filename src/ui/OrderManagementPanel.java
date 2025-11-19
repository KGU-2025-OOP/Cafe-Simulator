package ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.util.Map;
import java.util.LinkedHashMap;

public class OrderManagementPanel extends JPanel {
	
    private static final String CONFIRM_BUTTON_TEXT = "발주 확인";
    private static final String CANCEL_BUTTON_TEXT = "취소";
    private static final String TOTAL_COST_LABEL_FORMAT = "총 주문 금액: %d 원";

    private Map<String, Integer> ingredientPrices;
    private Map<String, JSpinner> spinnerMap;
    private JLabel totalCostLabel;
    private int finalTotalCost;
    private JButton confirmButton;
    private JButton cancelButton;

    public OrderManagementPanel(Map<String, Integer> ingredients) {
        ingredientPrices = ingredients;
        spinnerMap = new LinkedHashMap<>();
        finalTotalCost = 0;
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(createIngredientPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createIngredientPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        for (Map.Entry<String, Integer> entry : ingredientPrices.entrySet()) {
            mainPanel.add(createIngredientRow(entry.getKey(), entry.getValue()));
        }
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        
        return scrollPane;
    }

    private JPanel createIngredientRow(String name, int price) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        String labelText = String.format("%s (개당 %d원)", name, Integer.valueOf(price));
        
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setPreferredSize(new Dimension(150, 20));
        
        rowPanel.add(nameLabel);
        
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 99, 1);
        
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(e -> updateTotalCost());
        spinnerMap.put(name, spinner);
        
        rowPanel.add(spinner);
        
        return rowPanel;
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        totalCostLabel = new JLabel();
        
        southPanel.add(totalCostLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
        cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        southPanel.add(buttonPanel, BorderLayout.EAST);
        
        updateTotalCost();
        
        return southPanel;
    }

    private void updateTotalCost() {
        int total = 0;
        
        for (Map.Entry<String, JSpinner> entry : spinnerMap.entrySet()) {
            String name = entry.getKey();
            JSpinner spinner = entry.getValue();
            
            int price = ingredientPrices.get(name);
            int quantity = (Integer) spinner.getValue();
            
            total += (price * quantity);
        }
        
        finalTotalCost = total;
        totalCostLabel.setText(String.format(TOTAL_COST_LABEL_FORMAT, Integer.valueOf(total)));
    }

    public void resetSpinners() {
        for (JSpinner spinner : spinnerMap.values()) {
            spinner.setValue(Integer.valueOf(0));
        }
        
        updateTotalCost();
    }

    public Map<String, Integer> getOrderQuantities() {
        Map<String, Integer> order = new LinkedHashMap<>();
        
        for (Map.Entry<String, JSpinner> entry : spinnerMap.entrySet()) {
            order.put(entry.getKey(), (Integer) entry.getValue().getValue());
        }
        
        return order;
    }

    public int getTotalCost() {
        return finalTotalCost;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}