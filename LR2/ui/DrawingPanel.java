package ui;

import javax.swing.JPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import model.AbstractShape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import java.util.HashMap;

public class DrawingPanel extends JPanel {
    private List<AbstractShape> shapes = new ArrayList<>();
    private AbstractShape currentShape = null;

    public DrawingPanel() {
        setBackground(Color.WHITE);
    }

    public void addShape(AbstractShape shape) {
        shapes.add(shape);
        repaint();
    }
    public List<AbstractShape> getShapes() {
        return shapes;
    }
    public AbstractShape getCurrentShape() {
        return this.currentShape;
    }
    public void setCurrentShape(AbstractShape shape) {
        this.currentShape = shape;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Map<RenderingHints.Key, Object> render = new HashMap<>();
        render.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(render);

        for(AbstractShape shape : getShapes()) {
            shape.draw(g2d);
        }

        if(getCurrentShape() != null) {
            getCurrentShape().draw(g2d);
        }
    }
}
