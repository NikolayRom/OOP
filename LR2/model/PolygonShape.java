package model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class PolygonShape extends AbstractShape {
    private List<Point> points;

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

    @Override
    public void draw(Graphics2D g2d) {
        
        if(points.size() < 2) {
            return;
        }
        
        Polygon pol = new Polygon();
        for(Point p : points) {
            pol.addPoint(p.x, p.y);
        }

        if(getParameters().getFillColor() != null) {
            g2d.setColor(getParameters().getFillColor());
            g2d.fillPolygon(pol);
        }
        applyStrokeAndColor(g2d);
        g2d.drawPolygon(pol);
    }

    @Override
    public AbstractShape cloneShape() {
        PolygonShape copy = new PolygonShape(getParameters().clone());
        for(Point p: points) {
            copy.addPoint(new Point(p));
        }
        return copy;
    }
}
