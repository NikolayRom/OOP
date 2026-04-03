package model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LineShape.class, name = "line"),
    @JsonSubTypes.Type(value = RectangleShape.class, name = "rectangle"),
    @JsonSubTypes.Type(value = EllipseShape.class, name = "ellipse"),
    @JsonSubTypes.Type(value = PolygonShape.class, name = "polygon"),
    @JsonSubTypes.Type(value = PolylineShape.class, name = "polyline")
})

public abstract class AbstractShape {
    private DrawingParameters parameters;

    protected boolean isSelected = false; 
    protected double rotationAngle = 0.0; 

    public AbstractShape() {}

    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }    

    public AbstractShape(DrawingParameters parameters) {
        this.parameters = parameters.clone();
    }

    public void rotate(double deltaRadians) {
        this.rotationAngle += deltaRadians;
    }
    public void setEndPoint(Point p) { }

    protected void drawSelectionFrame(Graphics2D g2d) {
        if (!isSelected) return;

        java.awt.Rectangle bounds = getBounds();
        
        float[] dash = {5.0f};
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        g2d.setColor(Color.GRAY);
        g2d.drawRect(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4);

        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(Color.WHITE);
        int s = 6; 
        int[] xs = {bounds.x - 2, bounds.x + bounds.width + 2, bounds.x - 2, bounds.x + bounds.width + 2};
        int[] ys = {bounds.y - 2, bounds.y - 2, bounds.y + bounds.height + 2, bounds.y + bounds.height + 2};
        
        for (int i = 0; i < 4; i++) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(xs[i] - s/2, ys[i] - s/2, s, s);
            g2d.setColor(Color.BLUE);
            g2d.drawRect(xs[i] - s/2, ys[i] - s/2, s, s);
        }
    }

    public DrawingParameters getParameters() {
        return this.parameters;
    }

    public void setParameters(DrawingParameters params) {
        this.parameters = params;
    }

    public double getRotationAngle() { return rotationAngle; }
    public void setRotationAngle(double angle) { this.rotationAngle = angle; }

    protected void applyStrokeAndColor(Graphics2D g2d) {
        g2d.setColor(parameters.getLineColor());
        g2d.setStroke(new BasicStroke(parameters.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    public abstract boolean contains(Point p);
    public abstract void draw(Graphics2D g2d);
    public abstract AbstractShape cloneShape();
    public abstract void move(int dx, int dy);
    public abstract java.awt.Rectangle getBounds();
    public abstract void scale(double factor);
}
