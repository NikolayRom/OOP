package service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

import model.AbstractShape;
import model.DrawingData;
import model.Layer;

import java.io.File;
import java.util.List;

public class SerializationManager {
    private ObjectMapper mapper;

    public SerializationManager() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public void registerPluginType(Class<? extends AbstractShape> clazz, String name) {
        mapper.registerSubtypes(new NamedType(clazz, name));
    }

    public void save(String filename, List<Layer> layers) throws Exception {
        DrawingData data = new DrawingData(layers);
        mapper.writeValue(new File(filename), data);
    }

    public List<Layer> load(String filename) throws Exception {
        DrawingData data = mapper.readValue(new File(filename), DrawingData.class);
        return data.getLayers();
    }
}
