package command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AbstractShape;
import model.DrawingParameters;
import ui.DrawingPanel;

public class ChangeParamsCommand implements Command {
    private Map<AbstractShape, DrawingParameters> oldParamsMap = new HashMap<>();
    private DrawingParameters newParams;
    private DrawingPanel panel;

    public ChangeParamsCommand(DrawingPanel panel, List<AbstractShape> selectedShapes, DrawingParameters newParams) {
        this.panel = panel;
        this.newParams = newParams.clone();
        
        for (AbstractShape s : selectedShapes) {
            oldParamsMap.put(s, s.getParameters().clone());
        }
    }

    @Override
    public void execute() {
        for (AbstractShape s : oldParamsMap.keySet()) {
            s.setParameters(newParams.clone());
        }
        panel.repaint();
    }

    @Override
    public void undo() {
        for (Map.Entry<AbstractShape, DrawingParameters> entry : oldParamsMap.entrySet()) {
            entry.getKey().setParameters(entry.getValue());
        }
        panel.repaint();
    }
}