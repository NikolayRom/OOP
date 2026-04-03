package plugins.impl;

import java.awt.*;
import java.awt.geom.Path2D;
import model.AbstractShape;
import model.DrawingParameters;

public class HexagonShape extends AbstractShape {
    private Point p1, p2;

    public HexagonShape() {}
    public HexagonShape(Point p1, Point p2, DrawingParameters params) {
        super(params);
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
    }

    public Point getP1() { return p1; }
    public void setP1(Point p) { this.p1 = p; }
    public Point getP2() { return p2; }
    public void setP2(Point p) { this.p2 = p; }

    @Override
    public void draw(Graphics2D g2d) {
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
        Rectangle b = getBounds();
        g2d.rotate(rotationAngle, b.getCenterX(), b.getCenterY());

        Path2D path = createHexPath();
        if (getParameters().getFillColor() != null) {
            g2d.setColor(getParameters().getFillColor());
            g2d.fill(path);
        }
        applyStrokeAndColor(g2d);
        g2d.draw(path);
        drawSelectionFrame(g2d);
        g2d.setTransform(oldTransform);
    }

    private Path2D createHexPath() {
        Rectangle b = getBounds();
        double cx = b.getCenterX(), cy = b.getCenterY();
        double rx = b.width / 2.0, ry = b.height / 2.0;
        Path2D path = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double x = cx + rx * Math.cos(angle);
            double y = cy + ry * Math.sin(angle);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.closePath();
        return path;
    }

    @Override public boolean contains(Point p) { return createHexPath().contains(p); }
    @Override public Rectangle getBounds() { return new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y)); }
    @Override public void move(int dx, int dy) { p1.translate(dx, dy); p2.translate(dx, dy); }
    @Override public void setEndPoint(Point p) { this.p2 = new Point(p); }
    @Override public void scale(double f) { 
        Rectangle b = getBounds(); double cx = b.getCenterX(), cy = b.getCenterY();
        p1.setLocation(cx + (p1.x - cx) * f, cy + (p1.y - cy) * f);
        p2.setLocation(cx + (p2.x - cx) * f, cy + (p2.y - cy) * f);
    }
    @Override public AbstractShape cloneShape() { return new HexagonShape(new Point(p1), new Point(p2), getParameters().clone()); }
}