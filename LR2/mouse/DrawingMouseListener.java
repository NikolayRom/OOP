package mouse;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;

import ui.*;
import model.AbstractShape;
import model.DrawingParameters;
import model.EllipseShape;
import model.LineShape;
import model.PolygonShape;
import model.PolylineShape;
import model.RectangleShape;
import model.ShapeType;

public class DrawingMouseListener extends MouseAdapter {
    private DrawingApp app;
    private DrawingPanel panel;

    private boolean isDrawingComplex = false;

    public DrawingMouseListener(DrawingApp app) {
        this.app = app;
        this.panel = app.getDrawingPanel();
    }

    public boolean getIsDrawingComplex() {
        return this.isDrawingComplex;
    }
    public void setIsDrawingComplex(boolean bool) {
        this.isDrawingComplex = bool;
    }
    public DrawingApp getApp() {
        return this.app;
    }
    public DrawingPanel getPanel() {
        return this.panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        ShapeType type = getApp().getCurrentShapeType();
        DrawingParameters parameters = getApp().getCurrentParameters();

        if(SwingUtilities.isRightMouseButton(e)) {
            if(getIsDrawingComplex() && getPanel().getCurrentShape() != null) {
                getPanel().addShape(getPanel().getCurrentShape());
                getPanel().setCurrentShape(null);
                setIsDrawingComplex(false);
            }
            return;
        }

        switch(type) {
            case LINE:
                getPanel().setCurrentShape(new LineShape(p, p, parameters));
                break;
            case RECTANGLE:
                getPanel().setCurrentShape(new RectangleShape(p, p, parameters));
                break;
            case ELLIPSE:
                getPanel().setCurrentShape(new EllipseShape(p, p, parameters));
                break;
            case POLYLINE:
            case POLYGON:
                if(!getIsDrawingComplex()) {
                    setIsDrawingComplex(true);
                    if(type == ShapeType.POLYGON) {
                        PolygonShape polygon = new PolygonShape(parameters);
                        polygon.addPoint(p);
                        polygon.addPoint(p);
                        getPanel().setCurrentShape(polygon);
                    } else {
                        PolylineShape polyline = new PolylineShape(parameters);
                        polyline.addPoint(p);
                        polyline.addPoint(p);
                        getPanel().setCurrentShape(polyline);
                    }
                } else {
                    AbstractShape current = getPanel().getCurrentShape();
                    if(current instanceof PolygonShape) {
                        ((PolygonShape) current).addPoint(p);
                    } else {
                        ((PolylineShape) current).addPoint(p);
                    }
                }
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updatePreview(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(getIsDrawingComplex()) {
            updatePreview(e.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            return;
        }

        ShapeType type = getApp().getCurrentShapeType();
        switch(type) {
            case LINE:
            case RECTANGLE:
            case ELLIPSE:
                if(getPanel().getCurrentShape() != null) {
                    getPanel().addShape(getPanel().getCurrentShape());
                    getPanel().setCurrentShape(null);
                }
                break;
            case POLYGON:
            case POLYLINE:
                break;
        }
    }

    private void updatePreview(Point p) {
        AbstractShape current = getPanel().getCurrentShape();
        if(current != null) {
            if(current instanceof LineShape) {
                ((LineShape) current).setEndPoint(p);
            } else if(current instanceof RectangleShape) {
                ((RectangleShape) current).setEndPoint(p);
            } else if(current instanceof EllipseShape) {
                ((EllipseShape) current).setEndPoint(p);
            } else if(current instanceof PolygonShape) {
                ((PolygonShape) current).updateLastPoint(p);
            } else if(current instanceof PolylineShape) {
                ((PolylineShape) current).updateLastPoint(p);
            }

            getPanel().repaint();
        }
    }
}
