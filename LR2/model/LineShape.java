package model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

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
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        java.awt.Rectangle b = getBounds();
        g2d.rotate(rotationAngle, b.getCenterX(), b.getCenterY());

        applyStrokeAndColor(g2d);
        g2d.drawLine(getStartPoint().x, getStartPoint().y, getEndPoint().x, getEndPoint().y);
        drawSelectionFrame(g2d);

        g2d.setTransform(oldTransform);
    }

    @Override
    public AbstractShape cloneShape() {
        return new LineShape(new Point(getStartPoint()), new Point(getEndPoint()), getParameters().clone());
    }

    @Override
    public void move(int dx, int dy) {
        getStartPoint().translate(dx, dy);
        getEndPoint().translate(dx, dy);
    }

    @Override
    public boolean contains(Point p) {
        java.awt.geom.Line2D line = new java.awt.geom.Line2D.Float(p1.x, p1.y, p2.x, p2.y);
        return line.ptSegDist(p) <= 5.0; 
    }

    @Override
    public java.awt.Rectangle getBounds() {
        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int width = Math.abs(p1.x - p2.x);
        int height = Math.abs(p1.y - p2.y);
        return new java.awt.Rectangle(x, y, width, height);
    }

    @Override
    public void scale(double factor) {
        Rectangle b = getBounds();
        double cx = b.getCenterX();
        double cy = b.getCenterY();
        
        p1.setLocation(cx + (p1.x - cx) * factor, cy + (p1.y - cy) * factor);
        p2.setLocation(cx + (p2.x - cx) * factor, cy + (p2.y - cy) * factor);
    }
}
