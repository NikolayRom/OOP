package model;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;

public class EllipseShape extends AbstractShape {
    private Point p1, p2;

    public EllipseShape(Point p1, Point p2, DrawingParameters parameters) {
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

        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        java.awt.Rectangle b = getBounds();
        g2d.rotate(rotationAngle, b.getCenterX(), b.getCenterY());

        int x = Math.min(getStartPoint().x, getEndPoint().x);
        int y = Math.min(getStartPoint().y, getEndPoint().y);
        int width = Math.abs(getStartPoint().x - getEndPoint().x);
        int height = Math.abs(getStartPoint().y - getEndPoint().y);

        if(getParameters().getFillColor() != null) {
            g2d.setColor(getParameters().getFillColor());
            g2d.fillOval(x, y, width, height);
        }

        applyStrokeAndColor(g2d);
        g2d.drawOval(x, y, width, height);
        drawSelectionFrame(g2d);

        g2d.setTransform(oldTransform);
    }

    @Override
    public AbstractShape cloneShape() {
        return new EllipseShape(new Point(getStartPoint()), new Point(getEndPoint()), getParameters().clone());
    }

    @Override
    public void move(int dx, int dy) {
        getStartPoint().translate(dx, dy);
        getEndPoint().translate(dx, dy);
    }

    @Override
    public boolean contains(Point p) {
        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int width = Math.abs(p1.x - p2.x);
        int height = Math.abs(p1.y - p2.y);
        
        Ellipse2D ellipse = new Ellipse2D.Float(x, y, width, height);
        return ellipse.contains(p);
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
