package plugins.impl;

import java.awt.Point;

import model.AbstractShape;
import model.DrawingParameters;
import plugins.ShapePlugin; 

public class TrapezoidPluginImpl implements ShapePlugin {
    @Override
    public String getShapeName() { return "Трапеция"; }

    @Override
    public AbstractShape createShape(Point p1, Point p2, DrawingParameters params) {
        return new TrapezoidShape(p1, p2, params);
    }
}
