package model;

import java.util.List;

public class DrawingData {
    private List<Layer> layers;

    public DrawingData() {} 
    public DrawingData(List<Layer> layers) { this.layers = layers; }

    public List<Layer> getLayers() { return layers; }
    public void setLayers(List<Layer> layers) { this.layers = layers; }
}
