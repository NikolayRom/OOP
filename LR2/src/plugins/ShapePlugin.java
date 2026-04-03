package plugins;

import java.awt.Point;

import model.AbstractShape;
import model.DrawingParameters;

public interface ShapePlugin {
    String getShapeName(); 
    AbstractShape createShape(Point p1, Point p2, DrawingParameters params);
}
