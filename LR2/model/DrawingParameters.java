package model;

import exception.CloneNotSupported;

import java.awt.Color;

public class DrawingParameters implements Cloneable {
    private Color lineColor;
    private Color fillColor;
    private float thickness;

    public DrawingParameters(Color lineColor, Color fillColor, float thickness) {
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        this.thickness = thickness;
    }

    public Color getLineColor() {
        return this.lineColor;
    }
    public Color getFillColor() {
        return this.fillColor;
    }
    public float getThickness() {
        return this.thickness;
    }

    @Override
    public DrawingParameters clone() {
        try{
            return (DrawingParameters) super.clone();
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError("<<<ERROR: Копирование не удалось>>>");
        }
    }
}
