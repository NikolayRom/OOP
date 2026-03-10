package model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public abstract class AbstractShape {
    private DrawingParameters parameters;

    public AbstractShape(DrawingParameters parameters) {
        this.parameters = parameters.clone();
    }

    public DrawingParameters getParameters() {
        return this.parameters;
    }

    protected void applyStrokeAndColor(Graphics2D g2d) {
        g2d.setColor(parameters.getLineColor());
        g2d.setStroke(new BasicStroke(parameters.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    public abstract void draw(Graphics2D g2d);
    public abstract AbstractShape cloneShape();
}
