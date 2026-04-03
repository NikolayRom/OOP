package command;

import java.util.ArrayList;
import java.util.List;

import model.AbstractShape;
import ui.DrawingPanel;

public class RotateCommand implements Command {
    private List<AbstractShape> shapes;
    private double deltaAngle;
    private DrawingPanel panel;

    public RotateCommand(DrawingPanel panel, List<AbstractShape> selectedShapes, double deltaAngle) {
        this.panel = panel;
        this.shapes = new ArrayList<>(selectedShapes);
        this.deltaAngle = deltaAngle;
    }

    @Override
    public void execute() {
        for (AbstractShape s : shapes) {
            s.rotate(deltaAngle);
        }
        panel.repaint();
    }

    @Override
    public void undo() {
        for (AbstractShape s : shapes) {
            s.rotate(-deltaAngle); 
        }
        panel.repaint();
    }
}
