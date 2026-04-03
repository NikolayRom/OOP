package plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Iterator;

public class PluginManager {
    public List<ShapePlugin> loadPlugins(String directoryPath) {
        List<ShapePlugin> plugins = new ArrayList<>();
        File dir = new File(directoryPath);
        
        ClassLoader parentLoader = ShapePlugin.class.getClassLoader();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));
        if (files == null) return plugins;

        for (File file : files) {
            try {
                URL jarUrl = file.toURI().toURL();
                URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, parentLoader);

                ServiceLoader<ShapePlugin> serviceLoader = ServiceLoader.load(ShapePlugin.class, loader);

                for (ShapePlugin plugin : serviceLoader) {
                    System.out.println("Успешно загружен плагин: " + plugin.getShapeName());
                    plugins.add(plugin);
                }
            } catch (Throwable t) {
                System.err.println("Ошибка в JAR " + file.getName() + ": " + t.getMessage());
                t.printStackTrace();
            }
        }
        return plugins;
    }
}
