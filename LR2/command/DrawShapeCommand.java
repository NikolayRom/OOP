package command;    
import ui.DrawingPanel;
import model.AbstractShape;
import model.Layer;
import java.util.List;
import java.util.ArrayList;

public class DrawShapeCommand implements Command {
    private DrawingPanel panel;
    private Layer layer;
    private List<AbstractShape> shapes;

    public DrawShapeCommand(DrawingPanel panel, Layer layer, List<AbstractShape> shapes) {
        this.panel = panel;
        this.layer = layer;
        this.shapes = new ArrayList<>(shapes); 
    }

    @Override
    public void execute() {
        layer.getShapes().addAll(shapes);
        panel.repaint();
    }

    @Override
    public void undo() {
        layer.getShapes().removeAll(shapes);
        panel.repaint();
    }
}