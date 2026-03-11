package ui;

import java.awt.*;
import javax.swing.*;

import model.DrawingParameters;
import model.ShapeType;
import mouse.DrawingMouseListener;

public class DrawingApp extends JFrame {
    private ShapeType currentShapeType = ShapeType.LINE;
    private Color currentLineColor = Color.BLACK;
    private Color currentFillColor = null;
    private float currentThickness  = 2.0f;

    private DrawingPanel drawingPanel;

    public DrawingApp() {
        setTitle("Paint");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        drawingPanel = new DrawingPanel();

        DrawingMouseListener mouseListener = new DrawingMouseListener(this);
        drawingPanel.addMouseListener(mouseListener);
        drawingPanel.addMouseMotionListener(mouseListener);

        add(createToolBar(), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
    }

    public Color getCurrentLineColor() {
        return this.currentLineColor;
    }
    public void setCurrentLineColor(Color color) {
        this.currentLineColor = color;
    }
    public Color getCurrentFillColor() {
        return this.currentFillColor;
    }
    public void setCurrentFillColor(Color color) {
        this.currentFillColor = color;
    }
    public float getCurrentThickness() {
        return this.currentThickness;
    }
    public void setCurrentThickness(float num) {
        this.currentThickness = num;
    }
    public ShapeType getCurrentShapeType() {
        return this.currentShapeType;
    }
    public void setCurrentShapeType(ShapeType type) {
        this.currentShapeType = type;
    }
    public DrawingPanel getDrawingPanel() {
        return this.drawingPanel;
    }
    public DrawingParameters getCurrentParameters() {
        return new DrawingParameters(getCurrentLineColor(), getCurrentFillColor(), getCurrentThickness());
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnLine = new JButton("Line");
        btnLine.addActionListener(e -> setCurrentShapeType(ShapeType.LINE));

        JButton btnRect = new JButton("Rectangle");
        btnRect.addActionListener(e -> setCurrentShapeType(ShapeType.RECTANGLE));

        JButton btnEllipse = new JButton("Ellipse");
        btnEllipse.addActionListener(e -> setCurrentShapeType(ShapeType.ELLIPSE));

        JButton btnPolyline = new JButton("Polyline");
        btnPolyline.addActionListener(e -> setCurrentShapeType(ShapeType.POLYLINE));

        JButton btnPolygon = new JButton("Polygon");
        btnPolygon.addActionListener(e -> setCurrentShapeType(ShapeType.POLYGON));

        JButton btnLineColor = new JButton("Line Color");
        btnLineColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose Line Color", getCurrentLineColor());
            if(color != null) {
                setCurrentLineColor(color);
            }
        });

        JButton btnFillColor = new JButton("Fill Color");
        btnFillColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose Fill Color", getCurrentFillColor());
            if(color != null) {
                setCurrentFillColor(color);
            }
        });

        JButton btnNoFill = new JButton("Remove Fill");
        btnNoFill.addActionListener(e -> setCurrentFillColor(null));

        JSpinner spinnerThickness = new JSpinner(new SpinnerNumberModel(2.0, 1.0, 20.0, 1.0));
        spinnerThickness.addChangeListener(e -> setCurrentThickness(((Double) spinnerThickness.getValue()).floatValue()));

        toolBar.add(btnLine);
        toolBar.add(btnRect);
        toolBar.add(btnEllipse);
        toolBar.add(btnPolyline);
        toolBar.add(btnPolygon);
        
        toolBar.addSeparator();

        toolBar.add(btnLineColor);
        toolBar.add(btnFillColor);
        toolBar.add(btnNoFill);

        toolBar.addSeparator();

        toolBar.add(new JLabel("Thickness"));
        toolBar.add(spinnerThickness);

        return toolBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingApp app = new DrawingApp();
            app.setVisible(true);
        });
    }
}
