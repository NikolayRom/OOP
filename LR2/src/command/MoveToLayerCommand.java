package command;

import java.util.List;

import model.AbstractShape;
import model.Layer;
import ui.DrawingPanel;

import java.util.ArrayList;

public class MoveToLayerCommand implements Command {
    private List<AbstractShape> shapes;
    private Layer from;
    private Layer to;
    private DrawingPanel panel;

    public MoveToLayerCommand(List<AbstractShape> shapes, Layer from, Layer to, DrawingPanel panel) {
        this.shapes = new ArrayList<>(shapes);
        this.from = from;
        this.to = to;
        this.panel = panel;
    }

    @Override
    public void execute() {
        from.getShapes().removeAll(shapes);
        to.getShapes().addAll(shapes);
        panel.repaint();
    }

    @Override
    public void undo() {
        to.getShapes().removeAll(shapes);
        from.getShapes().addAll(shapes);
        panel.repaint();
    }
}