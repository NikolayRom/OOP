package model;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.Rectangle;

public class PolylineShape extends AbstractShape {
    private List<Point> points;

    public PolylineShape() {}

    public PolylineShape(DrawingParameters parameters) {
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

    @Override
    public void setEndPoint(Point p2) {
        this.points.add(new Point(p2));
    }

    public List<Point> getPoints() {
        return this.points;
    }
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public void draw(Graphics2D g2d) {
        
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        java.awt.Rectangle b = getBounds();
        g2d.rotate(rotationAngle, b.getCenterX(), b.getCenterY());


        if(points.size() < 2) {
            return;
        }
        
        int nPoints = points.size();
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];

        for(int i = 0; i < nPoints; i ++) {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
        }

        applyStrokeAndColor(g2d);
        g2d.drawPolyline(xPoints, yPoints, nPoints);
        drawSelectionFrame(g2d);

        g2d.setTransform(oldTransform);
    }

    @Override
    public AbstractShape cloneShape() {
        PolylineShape copy = new PolylineShape(getParameters().clone());
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
        if (points.size() < 2) return false;

        for (int i = 0; i < points.size() - 1; i++) {
            Point pStart = points.get(i);
            Point pEnd = points.get(i + 1);
            
            Line2D segment = new Line2D.Float(pStart.x, pStart.y, pEnd.x, pEnd.y);
            
            if (segment.ptSegDist(p) <= 5.0) {
                return true;
            }
        }
        return false;
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
