package model;

import java.awt.Graphics2D;
import java.awt.Point;

public class RectangleShape extends AbstractShape {
    private Point p1, p2;

    public RectangleShape(Point p1, Point p2, DrawingParameters parameters) {
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

    public void draw(Graphics2D g2d) {
        int x = Math.min(getStartPoint().x, getEndPoint().x);
        int y = Math.min(getStartPoint().y, getEndPoint().y);
        int width = Math.abs(getStartPoint().x - getEndPoint().x);
        int height = Math.abs(getStartPoint().y - getEndPoint().y);

        if(getParameters().getFillColor() != null) {
            g2d.setColor(getParameters().getFillColor());
            g2d.fillRect(x, y, width, height);
        }

        applyStrokeAndColor(g2d);
        g2d.drawRect(x, y, width, height);
    }

    @Override
    public AbstractShape cloneShape() {
        return new RectangleShape(new Point(getStartPoint()), new Point(getEndPoint()), getParameters().clone());
    }
}
