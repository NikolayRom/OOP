package model;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Graphics2D;

public class PolylineShape extends AbstractShape {
    private List<Point> points;

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
    public void draw(Graphics2D g2d) {
        
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
}
