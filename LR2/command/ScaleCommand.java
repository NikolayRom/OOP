package command;

import java.util.List;
import model.AbstractShape;
import ui.DrawingPanel;
import java.util.ArrayList;

public class ScaleCommand implements Command {
    private List<AbstractShape> shapes;
    private double factor;
    private DrawingPanel panel;

    public ScaleCommand(DrawingPanel panel, List<AbstractShape> shapes, double factor) {
        this.panel = panel;
        this.shapes = new ArrayList<>(shapes);
        this.factor = factor;
    }

    @Override public void execute() { for (AbstractShape s : shapes) s.scale(factor); panel.repaint(); }
    @Override public void undo() { for (AbstractShape s : shapes) s.scale(1.0 / factor); panel.repaint(); }
}
