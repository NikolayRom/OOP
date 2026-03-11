package ui;

import javax.swing.JPanel;

import java.awt.Color;
import model.AbstractShape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class DrawingPanel extends JPanel {
    private Stack<AbstractShape> shapes = new Stack<>();
    private Stack<AbstractShape> redoShapes = new Stack<>();
    private AbstractShape currentShape = null;

    public DrawingPanel() {
        setBackground(Color.WHITE);
    }

    public void addShape(AbstractShape shape) {
        shapes.push(shape);
        redoShapes.clear();
        repaint();
    }
    public Stack<AbstractShape> getShapes() {
        return shapes;
    }
    public AbstractShape getCurrentShape() {
        return this.currentShape;
    }
    public void setCurrentShape(AbstractShape shape) {
        this.currentShape = shape;
    }

    public void undo() {
        if(!shapes.isEmpty()) {
            redoShapes.push(shapes.pop());
            repaint();
        }
    }

    public void redo() {
        if(!redoShapes.isEmpty()) {
            shapes.push(redoShapes.pop());
            repaint();
        }
    }

    public void copyLastShape() {
        if(!shapes.isEmpty()) {
            AbstractShape clone = shapes.peek().cloneShape();
            clone.move(20, 20);
            addShape(clone);
        }
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
