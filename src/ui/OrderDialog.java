package ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

public class OrderDialog extends JDialog
{
    private static final String DIALOG_TITLE = "재료 발주";
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
    private boolean isConfirmed;
    private int finalTotalCost;

    public OrderDialog(JFrame parent, Map<String, Integer> ingredients)
    {
        super(parent, DIALOG_TITLE, true);

        this.ingredientPrices = ingredients;
        this.spinnerMap = new LinkedHashMap<>();
        this.isConfirmed = false;
        this.finalTotalCost = 0;

        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(
                DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        add(createIngredientPanel(), BorderLayout.CENTER);

        add(createSouthPanel(), BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private JScrollPane createIngredientPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        for (Map.Entry<String, Integer> entry : this.ingredientPrices.entrySet())
        {
            String name = entry.getKey();
            int price = entry.getValue();
            mainPanel.add(createIngredientRow(name, price));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(ITEM_PANEL_WIDTH, ITEM_PANEL_HEIGHT));

        return scrollPane;
    }

    private JPanel createIngredientRow(String name, int price)
    {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // [수정] String.format()을 Integer.valueOf()로 감싸기
        String labelText = String.format("%s (개당 %d원)", name, Integer.valueOf(price));
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setPreferredSize(new Dimension(150, 20));
        rowPanel.add(nameLabel);

        SpinnerNumberModel model = new SpinnerNumberModel(
                SPINNER_MIN_VALUE,
                SPINNER_MIN_VALUE,
                SPINNER_MAX_VALUE,
                SPINNER_STEP_VALUE
        );
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

        JButton confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
        confirmButton.addActionListener(e -> onConfirm());

        JButton cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        southPanel.add(buttonPanel, BorderLayout.EAST);

        updateTotalCost();

        return southPanel;
    }

    private void updateTotalCost()
    {
        int total = 0;
        for (Map.Entry<String, JSpinner> entry : this.spinnerMap.entrySet())
        {
            String name = entry.getKey();
            JSpinner spinner = entry.getValue();

            int price = this.ingredientPrices.get(name);
            int quantity = (Integer) spinner.getValue();

            total += (price * quantity);
        }

        this.finalTotalCost = total;
        // [수정] String.format()을 Integer.valueOf()로 감싸기
        this.totalCostLabel.setText(String.format(TOTAL_COST_LABEL_FORMAT, Integer.valueOf(total)));
    }

    private void onConfirm()
    {
        this.isConfirmed = true;
        dispose();
    }

    private void onCancel()
    {
        this.isConfirmed = false;
        dispose();
    }

    public boolean isConfirmed()
    {
        return this.isConfirmed;
    }

    public Map<String, Integer> getOrderQuantities()
    {
        Map<String, Integer> order = new LinkedHashMap<>();
        if (this.isConfirmed)
        {
            for (Map.Entry<String, JSpinner> entry : this.spinnerMap.entrySet())
            {
                order.put(entry.getKey(), (Integer) entry.getValue().getValue());
            }
        }
        return order;
    }

    public int getTotalCost()
    {
        return this.finalTotalCost;
    }
}