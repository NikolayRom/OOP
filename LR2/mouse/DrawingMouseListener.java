package mouse;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.Point;
import java.util.Collections;

import ui.*;
import model.AbstractShape;
import model.DrawingParameters;
import model.EllipseShape;
import model.LineShape;
import model.PolygonShape;
import model.PolylineShape;
import model.RectangleShape;
import model.ShapeType;
import command.*;

public class DrawingMouseListener extends MouseAdapter {
    private DrawingApp app;
    private DrawingPanel panel;
    private Point lastMousePoint;
    private Point startDragPoint;
    private boolean isDrawingComplex = false;

    public DrawingMouseListener(DrawingApp app) {
        this.app = app;
        this.panel = app.getDrawingPanel();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        ShapeType type = app.getCurrentShapeType();
        DrawingParameters parameters = app.getCurrentParameters();
        lastMousePoint = p;
        startDragPoint = p;

        if (SwingUtilities.isRightMouseButton(e)) {
            if (isDrawingComplex && panel.getCurrentShape() != null) {
                finishDrawing();
            }
            return;
        }

        if (type == ShapeType.CURSOR) {
            if (!e.isShiftDown()) app.clearSelection();
            
            List<AbstractShape> allShapes = panel.getAllShapes();
            for (int i = allShapes.size() - 1; i >= 0; i--) {
                if (allShapes.get(i).contains(p)) {
                    app.addToSelection(allShapes.get(i));
                    break; 
                }
            }
            panel.repaint();
            return;
        }

        switch (type) {
            case CURSOR: break;
            case LINE: panel.setCurrentShape(new LineShape(p, p, parameters)); break;
            case RECTANGLE: panel.setCurrentShape(new RectangleShape(p, p, parameters)); break;
            case ELLIPSE: panel.setCurrentShape(new EllipseShape(p, p, parameters)); break;
            case POLYLINE:
            case POLYGON:
                if (!isDrawingComplex) {
                    isDrawingComplex = true;
                    if (type == ShapeType.POLYGON) {
                        PolygonShape poly = new PolygonShape(parameters);
                        poly.addPoint(p); poly.addPoint(p);
                        panel.setCurrentShape(poly);
                    } else {
                        PolylineShape line = new PolylineShape(parameters);
                        line.addPoint(p); line.addPoint(p);
                        panel.setCurrentShape(line);
                    }
                } else {
                    AbstractShape curr = panel.getCurrentShape();
                    if (curr instanceof PolygonShape) ((PolygonShape) curr).addPoint(p);
                    else if (curr instanceof PolylineShape) ((PolylineShape) curr).addPoint(p);
                }
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point currentPoint = e.getPoint();

        if (app.getCurrentShapeType() == ShapeType.CURSOR && !app.getSelectedShapes().isEmpty()) {
            int dx = currentPoint.x - lastMousePoint.x;
            int dy = currentPoint.y - lastMousePoint.y;
            
            for (AbstractShape shape : app.getSelectedShapes()) {
                shape.move(dx, dy);
            }
            
            lastMousePoint = currentPoint;
            panel.repaint();
        } else {
            updatePreview(currentPoint);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isDrawingComplex) updatePreview(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) return;

        if (app.getCurrentShapeType() == ShapeType.CURSOR) {
            int dx = e.getX() - startDragPoint.x;
            int dy = e.getY() - startDragPoint.y;
            if (dx != 0 || dy != 0) {
                app.getCommandHistory().push(new MoveCommand(app.getSelectedShapes(), dx, dy));
            }
        } else {
            ShapeType t = app.getCurrentShapeType();
            if (t == ShapeType.LINE || t == ShapeType.RECTANGLE || t == ShapeType.ELLIPSE) {
                finishDrawing();
            }
        }
    }

    private void finishDrawing() {
        AbstractShape s = panel.getCurrentShape();
        if (s != null) {
            List<AbstractShape> list = Collections.singletonList(s);
            Command cmd = new DrawShapeCommand(panel, panel.getActiveLayer(), list);
            app.getCommandHistory().execute(cmd);
            panel.setCurrentShape(null);
            isDrawingComplex = false;
        }
    }

    private void updatePreview(Point p) {
        AbstractShape s = panel.getCurrentShape();
        if (s == null) return;
        if (s instanceof LineShape) ((LineShape) s).setEndPoint(p);
        else if (s instanceof RectangleShape) ((RectangleShape) s).setEndPoint(p);
        else if (s instanceof EllipseShape) ((EllipseShape) s).setEndPoint(p);
        else if (s instanceof PolygonShape) ((PolygonShape) s).updateLastPoint(p);
        else if (s instanceof PolylineShape) ((PolylineShape) s).updateLastPoint(p);
        panel.repaint();
    }
}