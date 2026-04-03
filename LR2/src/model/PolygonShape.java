package model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class PolygonShape extends AbstractShape {
    private List<Point> points;

    public PolygonShape() {}

    public PolygonShape(DrawingParameters parameters) {
        super(parameters);
        this.points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        points.add(new Point(p));
    }
    public void updateLastPoint(Point p) {
        if(!points.isEmpty()) {
            points.set(points.size() - 1, p);
        }
    }

    public List<Point> getPoints() {
        return this.points;
    }
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public void setEndPoint(Point p2) {
        this.points.add(new Point(p2));
    }

    @Override
    public void draw(Graphics2D g2d) {

        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        java.awt.Rectangle b = getBounds();
        g2d.rotate(rotationAngle, b.getCenterX(), b.getCenterY());

        
        if(points.size() < 2) {
            return;
        }

        Path2D path = new Path2D.Float();
        path.setWindingRule(Path2D.WIND_NON_ZERO);

        Point firstPoint = points.get(0);
        path.moveTo(firstPoint.x, firstPoint.y);

        for(Point point : points) {
            path.lineTo(point.x, point.y);
        }

        path.closePath();

        if(getParameters().getFillColor() != null) {
            g2d.setColor(getParameters().getFillColor());
            g2d.fill(path);
        }

        applyStrokeAndColor(g2d);
        g2d.draw(path);
        drawSelectionFrame(g2d);

        g2d.setTransform(oldTransform);
    }

    @Override
    public AbstractShape cloneShape() {
        PolygonShape copy = new PolygonShape(getParameters().clone());
        for(Point p: points) {
            copy.addPoint(new Point(p));
        }
        return copy;
    }

    @Override
    public void move(int dx, int dy) {
        for(Point p : points) {
            p.translate(dx, dy);
        }
    }

    @Override
    public boolean contains(Point p) {
        Path2D path = new Path2D.Float();
        path.setWindingRule(Path2D.WIND_NON_ZERO);
        if (points.isEmpty()) return false;
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.closePath();
        return path.contains(p);
    }

    @Override
    public java.awt.Rectangle getBounds() {
        if (points.isEmpty()) return new java.awt.Rectangle(0, 0, 0, 0);

        int minX = points.get(0).x;
        int maxX = points.get(0).x;
        int minY = points.get(0).y;
        int maxY = points.get(0).y;

        for (Point p : points) {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        return new java.awt.Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public void scale(double factor) {
        Rectangle b = getBounds();
        double cx = b.getCenterX();
        double cy = b.getCenterY();
        for (Point p : points) {
            p.setLocation(cx + (p.x - cx) * factor, cy + (p.y - cy) * factor);
        }
    }
}
