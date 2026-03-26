package model;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private String name;
    private List<AbstractShape> shapes = new ArrayList<>();
    private boolean visible = true; 

    public Layer(String name) { this.name = name; }
    public String getName() { return name; }
    public List<AbstractShape> getShapes() { return shapes; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public String toString() { 
        return name + (visible ? "" : " (Скрыт)"); 
    }
}