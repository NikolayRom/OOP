package ui;

import javax.swing.JPanel;

import java.awt.Color;
import model.AbstractShape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import model.Layer;
import java.util.ArrayList;

public class DrawingPanel extends JPanel {
    private List<Layer> layers = new ArrayList<>();
    private int activeLayerIndex = 0;
    private AbstractShape currentShape = null;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        layers.add(new Layer("Слой 1")); // Всегда есть хотя бы один слой
    }

    public List<Layer> getLayers() { return layers; }
    public Layer getActiveLayer() { return layers.get(activeLayerIndex); }
    public void setActiveLayerIndex(int index) { this.activeLayerIndex = index; }

    // Метод получения ВСЕХ фигур со ВСЕХ слоев для поиска выделения
    public List<AbstractShape> getAllShapes() {
        List<AbstractShape> all = new ArrayList<>();
        for (Layer l : layers) all.addAll(l.getShapes());
        return all;
    }

    public AbstractShape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(AbstractShape currentShape) {
        this.currentShape = currentShape;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Map<RenderingHints.Key, Object> render = new HashMap<>();
        render.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(render);

        for (Layer layer : layers) {
            if (layer.isVisible()) {
                for (AbstractShape shape : layer.getShapes()) {
                    shape.draw(g2d);
                }
            }
        }

        if (currentShape != null) {
            currentShape.draw((Graphics2D) g);
        }
    }
}
