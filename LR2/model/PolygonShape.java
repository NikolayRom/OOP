package model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
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
}
