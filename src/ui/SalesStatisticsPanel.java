package ui;

import core.GameCanvas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SalesStatisticsPanel extends JPanel {

    private JButton backButton;
    private GraphDisplayPanel graphDisplayPanel;

    private Map<Integer, Integer> dailySalesHistory;

    private Image backgroundImage;

    public SalesStatisticsPanel(Map<Integer, Integer> dailySalesHistory) {
        this.dailySalesHistory = dailySalesHistory;

        backgroundImage = ImageManager.getImage(ImageManager.IMG_MENU_BG);

        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setPreferredSize(ScreenConfig.FRAME_SIZE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        backButton = new JButton(ImageManager.getImageIcon(ImageManager.BTN_BACK));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel backButtonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonWrapper.setOpaque(false);
        backButtonWrapper.add(backButton);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("성장도 그래프          ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                String text = getText();
                FontMetrics fm = g2.getFontMetrics();

                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawString(text, x + 3, y + 3);

                g2.setColor(new Color(160, 110, 80));
                g2.drawString(text, x, y);
            }
        };

        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setPreferredSize(new Dimension(250, 60));

        topPanel.add(backButtonWrapper, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        graphDisplayPanel = new GraphDisplayPanel(this.dailySalesHistory);
        add(graphDisplayPanel, BorderLayout.CENTER);
    }

    public JButton getBackButton() {
        return backButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class GraphDisplayPanel extends JPanel {
        private static final int PADDING = 60;
        private static final int POINT_RADIUS = 5;
        private static final int CLICK_RADIUS = POINT_RADIUS + 3;
        private static final int X_AXIS_DAYS = 16;
        private static final int Y_AXIS_INTERVAL = 10000;

        private static final Color COLOR_BACKGROUND = new Color(248, 248, 248);
        private static final Color COLOR_LINE = new Color(139, 69, 19);
        private static final Color COLOR_POINT = new Color(210, 180, 140);
        private static final Color COLOR_AXIS = Color.BLACK;
        private static final Color COLOR_GRID = new Color(220, 220, 220);

        private static final Stroke STROKE_LINE = new BasicStroke(2.5f);
        private static final Stroke STROKE_AXIS = new BasicStroke(1.5f);
        private static final Stroke STROKE_GRID = new BasicStroke(1f);

        private Map<Integer, Integer> historyData;

        private List<Integer> salesData;
        private List<Point> graphPoints;

        public GraphDisplayPanel(Map<Integer, Integer> historyData) {
            this.historyData = historyData;
            salesData = new ArrayList<>();
            graphPoints = new ArrayList<>();

            File salesSave = new File(GameCanvas.REVENUE_SAVE_PATH);
            if (salesSave.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(salesSave));
                    String in;
                    do {
                        in = br.readLine();
                        if (in == null) {
                            break;
                        }
                        String[] temp = in.split(" ");
                        int day = Integer.parseInt(temp[0]);
                        int revenue = Integer.parseInt(temp[1]);
                        historyData.put(day, revenue);
                    } while (in != null);
                    br.close();
                } catch (FileNotFoundException e) {
                    assert (true);
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    assert (true);
                    throw new RuntimeException(e);
                }
            }

            setBackground(COLOR_BACKGROUND);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleGraphClick(e.getX(), e.getY());
                }
            });
        }

        private void handleGraphClick(int clickX, int clickY) {
            for (int i = 0; i < graphPoints.size(); i++) {
                Point p = graphPoints.get(i);

                double distance = Math.sqrt(Math.pow(clickX - p.x, 2) + Math.pow(clickY - p.y, 2));

                if (distance <= CLICK_RADIUS) {
                    int day = i + 1;
                    int sales = salesData.get(i);

                    String message = String.format("%d일차 수익: %,d 원", day, sales);

                    JOptionPane.showMessageDialog(this, message, "일일 매출 정보", JOptionPane.INFORMATION_MESSAGE);

                    return;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            salesData = new ArrayList<>(historyData.values());
            graphPoints.clear();

            if (salesData.isEmpty()) {
                drawCenteredString(g2, "아직 매출 데이터가 없습니다.");
                return;
            }

            int graphWidth = getWidth() - 2 * PADDING;
            int graphHeight = getHeight() - 2 * PADDING;

            int maxSales = salesData.isEmpty() ? 0 : Collections.max(salesData);
            int minSales = salesData.isEmpty() ? 0 : Collections.min(salesData);

            int yMax = (int) (Math.ceil(Math.max(maxSales, 0) / (double) Y_AXIS_INTERVAL) * Y_AXIS_INTERVAL);
            int yMin = (int) (Math.floor(Math.min(minSales, 0) / (double) Y_AXIS_INTERVAL) * Y_AXIS_INTERVAL);

            if (yMax == 0 && maxSales > 0) yMax = Y_AXIS_INTERVAL;
            if (yMax == 0 && yMin == 0) yMax = Y_AXIS_INTERVAL;

            int yRange = yMax - yMin;
            if (yRange == 0) yRange = 1;

            g2.setColor(COLOR_AXIS);
            g2.setStroke(STROKE_AXIS);
            g2.drawLine(PADDING, PADDING, PADDING, PADDING + graphHeight);
            g2.drawLine(PADDING, PADDING + graphHeight, PADDING + graphWidth, PADDING + graphHeight);

            g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
            for (int yVal = yMin; yVal <= yMax; yVal += Y_AXIS_INTERVAL) {
                int yScreen = PADDING + (int) ((double) (yMax - yVal) * graphHeight / yRange);

                g2.setColor(COLOR_GRID);
                g2.setStroke(STROKE_GRID);
                g2.drawLine(PADDING, yScreen, PADDING + graphWidth, yScreen);

                g2.setColor(COLOR_AXIS);
                g2.drawString(String.format("%,d", yVal), PADDING - 50, yScreen + 5);
            }

            g2.setColor(COLOR_AXIS);
            g2.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
            double xStep = (double) graphWidth / (X_AXIS_DAYS - 1);

            for (int i = 0; i < X_AXIS_DAYS; i++) {
                int x = PADDING + (int) (i * xStep);
                g2.drawString((i + 1) + "일", x - 5, PADDING + graphHeight + 25);
            }

            for (int i = 0; i < salesData.size(); i++) {
                if (i >= X_AXIS_DAYS) break;

                int x = PADDING + (int) (i * xStep);
                int y = PADDING + (int) ((double) (yMax - salesData.get(i)) * graphHeight / yRange);

                graphPoints.add(new Point(x, y));
            }

            g2.setColor(COLOR_LINE);
            g2.setStroke(STROKE_LINE);
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                Point p1 = graphPoints.get(i);
                Point p2 = graphPoints.get(i + 1);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            g2.setColor(COLOR_POINT);
            for (Point p : graphPoints) {
                g2.fillOval(p.x - POINT_RADIUS, p.y - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
            }
        }

        private void drawCenteredString(Graphics2D g2, String text) {
            Font font = new Font("Malgun Gothic", Font.BOLD, 24);
            g2.setFont(font);
            g2.setColor(Color.GRAY);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(text, x, y);
        }
    }
}