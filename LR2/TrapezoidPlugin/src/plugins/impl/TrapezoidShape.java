package plugins.impl;

import java.awt.*;
import java.awt.geom.Path2D;
import model.AbstractShape;
import model.DrawingParameters;

public class TrapezoidShape extends AbstractShape {
    private Point p1;
    private Point p2;

    public TrapezoidShape() {} 

    public TrapezoidShape(Point p1, Point p2, DrawingParameters params) {
        super(params);
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
    }

    public Point getP1() { return p1; }
    public void setP1(Point p) { this.p1 = p; }
    public Point getP2() { return p2; }
    public void setP2(Point p) { this.p2 = p; }

    @Override
    public void setEndPoint(Point p2) {
        this.p2 = new Point(p2);
    }

    @Override
    public void draw(Graphics2D g2d) {
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
        Rectangle b = getBounds();
        Path2D path = createTrapezoidPath();
        g2d.rotate(rotationAngle, b.getCenterX(), b.getCenterY());

        if (getParameters().getFillColor() != null) {
            g2d.setColor(getParameters().getFillColor());
            g2d.fill(path); 
        }

        applyStrokeAndColor(g2d);
        g2d.draw(path);

        drawSelectionFrame(g2d);

        g2d.setTransform(oldTransform);
    }

    private Path2D createTrapezoidPath() {
        int x1 = Math.min(p1.x, p2.x);
        int y1 = Math.min(p1.y, p2.y);
        int x2 = Math.max(p1.x, p2.x);
        int y2 = Math.max(p1.y, p2.y);
        int w = x2 - x1;
        Path2D path = new Path2D.Float();
        path.moveTo(x1 + w * 0.2, y1); 
        path.lineTo(x2 - w * 0.2, y1); 
        path.lineTo(x2, y2);           
        path.lineTo(x1, y2);          
        path.closePath();
        return path;
    }

    @Override
    public boolean contains(Point p) {
        return createTrapezoidPath().contains(p);
    }

    @Override
    public Rectangle getBounds() {
        return createTrapezoidPath().getBounds();
    }

    @Override
    public void move(int dx, int dy) {
        p1.translate(dx, dy);
        p2.translate(dx, dy);
    }

    @Override
    public void scale(double factor) {
        Rectangle b = getBounds();
        double cx = b.getCenterX();
        double cy = b.getCenterY();
        p1.setLocation(cx + (p1.x - cx) * factor, cy + (p1.y - cy) * factor);
        p2.setLocation(cx + (p2.x - cx) * factor, cy + (p2.y - cy) * factor);
    }

    @Override
    public AbstractShape cloneShape() {
        return new TrapezoidShape(new Point(p1), new Point(p2), getParameters().clone());
    }
}