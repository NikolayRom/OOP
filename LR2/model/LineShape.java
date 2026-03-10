package model;

import java.awt.Graphics2D;
import java.awt.Point;

public class LineShape extends AbstractShape {
    private Point p1, p2;

    public LineShape(Point p1, Point p2, DrawingParameters parameters) {
        super(parameters);
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
    }

    public Point getStartPoint() {
        return this.p1;
    }
    public Point getEndPoint() {
        return this.p2;
    }

    public void setEndPoint(Point p2) {
        this.p2 = p2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        applyStrokeAndColor(g2d);
        g2d.drawLine(getStartPoint().x, getStartPoint().y, getEndPoint().x, getEndPoint().y);
    }

    @Override
    public AbstractShape cloneShape() {
        return new LineShape(new Point(getStartPoint()), new Point(getEndPoint()), getParameters().clone());
    }
}
