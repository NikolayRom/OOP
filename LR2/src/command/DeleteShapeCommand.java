package command;

import java.util.ArrayList;
import java.util.List;

import model.AbstractShape;
import model.Layer;
import ui.DrawingPanel;

public class DeleteShapeCommand implements Command {
    private class DeletionRecord {
        AbstractShape shape;
        Layer layer;
        int index;

        DeletionRecord(AbstractShape s, Layer l, int i) {
            this.shape = s;
            this.layer = l;
            this.index = i;
        }
    }

    private List<DeletionRecord> records = new ArrayList<>();
    private DrawingPanel panel;

    public DeleteShapeCommand(DrawingPanel panel, List<AbstractShape> shapesToDelete) {
        this.panel = panel;
        for (AbstractShape s : shapesToDelete) {
            for (Layer l : panel.getLayers()) {
                int idx = l.getShapes().indexOf(s);
                if (idx != -1) {
                    records.add(new DeletionRecord(s, l, idx));
                    break;
                }
            }
        }
    }

    @Override
    public void execute() {
        for (DeletionRecord rec : records) {
            rec.layer.getShapes().remove(rec.shape);
        }
        panel.repaint();
    }

    @Override
    public void undo() {
        for (int i = records.size() - 1; i >= 0; i--) {
            DeletionRecord rec = records.get(i);
            rec.layer.getShapes().add(rec.index, rec.shape);
        }
        panel.repaint();
    }
}