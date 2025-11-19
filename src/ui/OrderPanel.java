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

    private Map<String, Integer> m_ingredientPrices;
    private Map<String, JSpinner> m_spinnerMap;
    private JLabel m_totalCostLabel;
    private int m_finalTotalCost;
    private JButton m_confirmButton;
    private JButton m_cancelButton;

    public OrderPanel(Map<String, Integer> ingredients)
    {
        m_ingredientPrices = ingredients;
        m_spinnerMap = new LinkedHashMap<>();
        m_finalTotalCost = 0;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(CreateIngredientPanel(), BorderLayout.CENTER);
        add(CreateSouthPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane CreateIngredientPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        for (Map.Entry<String, Integer> entry : m_ingredientPrices.entrySet())
        {
            mainPanel.add(CreateIngredientRow(entry.getKey(), entry.getValue()));
        }
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        return scrollPane;
    }

    private JPanel CreateIngredientRow(String name, int price)
    {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String labelText = String.format("%s (개당 %d원)", name, Integer.valueOf(price));
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setPreferredSize(new Dimension(150, 20));
        rowPanel.add(nameLabel);
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 99, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(e -> UpdateTotalCost());
        m_spinnerMap.put(name, spinner);
        rowPanel.add(spinner);
        return rowPanel;
    }

    private JPanel CreateSouthPanel()
    {
        JPanel southPanel = new JPanel(new BorderLayout());
        m_totalCostLabel = new JLabel();
        southPanel.add(m_totalCostLabel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        m_confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
        m_cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        buttonPanel.add(m_cancelButton);
        buttonPanel.add(m_confirmButton);
        southPanel.add(buttonPanel, BorderLayout.EAST);
        UpdateTotalCost();
        return southPanel;
    }

    private void UpdateTotalCost()
    {
        int total = 0;
        for (Map.Entry<String, JSpinner> entry : m_spinnerMap.entrySet())
        {
            String name = entry.getKey();
            JSpinner spinner = entry.getValue();
            int price = m_ingredientPrices.get(name);
            int quantity = (Integer) spinner.getValue();
            total += (price * quantity);
        }
        m_finalTotalCost = total;
        m_totalCostLabel.setText(String.format(TOTAL_COST_LABEL_FORMAT, Integer.valueOf(total)));
    }

    public void ResetSpinners()
    {
        for (JSpinner spinner : m_spinnerMap.values())
        {
            spinner.setValue(Integer.valueOf(0));
        }
        UpdateTotalCost();
    }

    public Map<String, Integer> GetOrderQuantities()
    {
        Map<String, Integer> order = new LinkedHashMap<>();
        for (Map.Entry<String, JSpinner> entry : m_spinnerMap.entrySet())
        {
            order.put(entry.getKey(), (Integer) entry.getValue().getValue());
        }
        return order;
    }

    public int GetTotalCost()
    {
        return m_finalTotalCost;
    }

    public JButton GetConfirmButton()
    {
        return m_confirmButton;
    }

    public JButton GetCancelButton()
    {
        return m_cancelButton;
    }
}