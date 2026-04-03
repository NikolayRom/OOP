package plugins.impl;

import java.awt.Point;
import plugins.ShapePlugin;
import model.AbstractShape;
import model.DrawingParameters;

public class HexagonPluginImpl implements ShapePlugin {
    @Override public String getShapeName() { return "Гексагон"; }
    @Override public AbstractShape createShape(Point p1, Point p2, DrawingParameters params) {
        return new HexagonShape(p1, p2, params);
    }
}