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

public class OrderPanel extends JPanel
{
    private static final String CONFIRM_BUTTON_TEXT = "발주 확인";
    private static final String CANCEL_BUTTON_TEXT = "취소";
    private static final String TOTAL_COST_LABEL_FORMAT = "총 주문 금액: %d 원";
    private static final int SPINNER_MIN_VALUE = 0;
    private static final int SPINNER_MAX_VALUE = 99;
    private static final int SPINNER_STEP_VALUE = 1;
    private static final int DIALOG_PADDING = 10;
    private static final int ITEM_PANEL_WIDTH = 350;
    private static final int ITEM_PANEL_HEIGHT = 200;

    private Map<String, Integer> ingredientPrices;
    private Map<String, JSpinner> spinnerMap;
    private JLabel totalCostLabel;
    private int finalTotalCost;
    private JButton confirmButton;
    private JButton cancelButton;

    public OrderPanel(Map<String, Integer> ingredients)
    {
        this.ingredientPrices = ingredients;
        this.spinnerMap = new LinkedHashMap<>();
        this.finalTotalCost = 0;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(
                DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));
        add(createIngredientPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createIngredientPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        for (Map.Entry<String, Integer> entry : this.ingredientPrices.entrySet()) {
            mainPanel.add(createIngredientRow(entry.getKey(), entry.getValue()));
        }
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(ITEM_PANEL_WIDTH, ITEM_PANEL_HEIGHT));
        return scrollPane;
    }

    private JPanel createIngredientRow(String name, int price)
    {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // [수정] String.format() 오류 수정
        String labelText = String.format("%s (개당 %d원)", name, Integer.valueOf(price));
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setPreferredSize(new Dimension(150, 20));
        rowPanel.add(nameLabel);
        SpinnerNumberModel model = new SpinnerNumberModel(
                SPINNER_MIN_VALUE, SPINNER_MIN_VALUE, SPINNER_MAX_VALUE, SPINNER_STEP_VALUE);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(e -> updateTotalCost());
        this.spinnerMap.put(name, spinner);
        rowPanel.add(spinner);
        return rowPanel;
    }

    private JPanel createSouthPanel()
    {
        JPanel southPanel = new JPanel(new BorderLayout());
        this.totalCostLabel = new JLabel();
        southPanel.add(this.totalCostLabel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
        this.cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        buttonPanel.add(this.cancelButton);
        buttonPanel.add(this.confirmButton);
        southPanel.add(buttonPanel, BorderLayout.EAST);
        updateTotalCost();
        return southPanel;
    }

    private void updateTotalCost()
    {
        int total = 0;
        for (Map.Entry<String, JSpinner> entry : this.spinnerMap.entrySet()) {
            String name = entry.getKey();
            JSpinner spinner = entry.getValue();
            int price = this.ingredientPrices.get(name);
            int quantity = (Integer) spinner.getValue();
            total += (price * quantity);
        }
        this.finalTotalCost = total;
        // [수정] String.format() 오류 수정
        this.totalCostLabel.setText(String.format(TOTAL_COST_LABEL_FORMAT, Integer.valueOf(total)));
    }

    /**
     * [신규] 모든 스피너의 값을 0으로 초기화하고 총합을 다시 계산합니다.
     */
    public void resetSpinners() {
        for (JSpinner spinner : this.spinnerMap.values()) {
            spinner.setValue(Integer.valueOf(0));
        }
        // updateTotalCost()는 스피너의 ChangeListener에 의해 자동으로 호출되지만,
        // 만약의 경우를 대비해 한 번 더 호출하거나, 여기서 직접 총합을 0으로 설정할 수 있습니다.
        updateTotalCost(); // 스피너 값이 0이 되었으므로 총합을 다시 계산
    }

    public Map<String, Integer> getOrderQuantities()
    {
        Map<String, Integer> order = new LinkedHashMap<>();
        for (Map.Entry<String, JSpinner> entry : this.spinnerMap.entrySet()) {
            order.put(entry.getKey(), (Integer) entry.getValue().getValue());
        }
        return order;
    }

    public int getTotalCost() { return this.finalTotalCost; }
    public JButton getConfirmButton() { return this.confirmButton; }
    public JButton getCancelButton() { return this.cancelButton; }
}