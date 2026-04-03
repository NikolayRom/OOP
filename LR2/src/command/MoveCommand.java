package command;
import java.util.List;

import model.AbstractShape;

import java.util.ArrayList;

public class MoveCommand implements Command {
    private List<AbstractShape> shapes;
    private int dx, dy;

    public MoveCommand(List<AbstractShape> shapes, int dx, int dy) {
        this.shapes = new ArrayList<>(shapes);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        for (AbstractShape s : shapes) s.move(dx, dy);
    }

    @Override
    public void undo() {
        for (AbstractShape s : shapes) s.move(-dx, -dy);
    }
}