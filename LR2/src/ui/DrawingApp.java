package ui;

import java.awt.*;
import javax.swing.*;
import java.util.Collections;

import command.*;
import model.AbstractShape;
import model.DrawingParameters;
import model.Layer;
import model.ShapeType;
import mouse.DrawingMouseListener;
import plugins.PluginManager;
import plugins.ShapePlugin;
import service.SerializationManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingApp extends JFrame {
    private ShapeType currentShapeType = ShapeType.LINE;
    private Color currentLineColor = Color.BLACK;
    private Color currentFillColor = null;
    private float currentThickness  = 2.0f;
    private CommandHistory commandHistory = new CommandHistory();
    private DefaultListModel<Layer> layerListModel;
    private JList<Layer> layerJList;              

    private SerializationManager serializationManager;
    private List<ShapePlugin> loadedPlugins = new ArrayList<>();
    private ShapePlugin activePlugin = null; 

    private DrawingPanel drawingPanel;

    private List<AbstractShape> selectedShapes = new ArrayList<>();

    public List<AbstractShape> getSelectedShapes() { return selectedShapes; }

    public void clearSelection() {
        for (AbstractShape s : selectedShapes) s.setSelected(false);
        selectedShapes.clear();
        drawingPanel.repaint();
    }

    public void addToSelection(AbstractShape shape) {
        if (!selectedShapes.contains(shape)) {
            shape.setSelected(true);
            selectedShapes.add(shape);
            drawingPanel.repaint();
        }
    }

    public DrawingApp() {

        serializationManager = new SerializationManager();

        PluginManager pm = new PluginManager();
        loadedPlugins = pm.loadPlugins("plugins");

        for (ShapePlugin plugin : loadedPlugins) {
            AbstractShape dummy = plugin.createShape(new Point(), new Point(), getCurrentParameters());
            serializationManager.registerPluginType(dummy.getClass(), dummy.getClass().getSimpleName());
        }

        setTitle("Paint");
        setSize(1400, 1100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        drawingPanel = new DrawingPanel();
        drawingPanel.setFocusable(true); 

        DrawingMouseListener mouseListener = new DrawingMouseListener(this);
        drawingPanel.addMouseListener(mouseListener);
        drawingPanel.addMouseMotionListener(mouseListener);

        add(createToolBar(), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);

        drawingPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    if (e.getKeyCode() == KeyEvent.VK_Z) {
                        commandHistory.undo();
                        drawingPanel.repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_Y) {
                        commandHistory.redo();
                        drawingPanel.repaint();
                    }
                    return;
                }

                if (selectedShapes.isEmpty()) return;

                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    Command delCmd = new DeleteShapeCommand(drawingPanel, getSelectedShapes());
                    commandHistory.execute(delCmd);
                    clearSelection(); 
                    return;
                }

                int dx = 0, dy = 0;
                if (e.getKeyCode() == KeyEvent.VK_UP) dy = -1;
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) dy = 1;
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) dx = -1;
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) dx = 1;

                if (dx != 0 || dy != 0) {
                    Command moveCmd = new MoveCommand(getSelectedShapes(), dx, dy);
                    commandHistory.execute(moveCmd);
                    drawingPanel.repaint();
                }
            }
        });

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawingPanel.requestFocusInWindow();
            }
        });

        this.layerListModel = new DefaultListModel<>();
        this.layerListModel.addElement(drawingPanel.getLayers().get(0));
        this.layerJList = new JList<>(this.layerListModel);
        this.layerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.layerJList.setSelectedIndex(0);

        this.layerJList.addListSelectionListener(e -> {
            int idx = this.layerJList.getSelectedIndex();
            if (idx != -1) drawingPanel.setActiveLayerIndex(idx);
        });

        JButton btnAddLayer = new JButton("+");
        btnAddLayer.addActionListener(e -> {
            Layer newLayer = new Layer("Слой " + (drawingPanel.getLayers().size() + 1));
            drawingPanel.getLayers().add(newLayer); 
            layerListModel.addElement(newLayer);   
            layerJList.setSelectedIndex(layerListModel.size() - 1);
        });

        JButton btnRemoveLayer = new JButton("-");
        btnRemoveLayer.addActionListener(e -> {
            int idx = layerJList.getSelectedIndex();
            if (idx != -1 && layerListModel.size() > 1) {
                drawingPanel.getLayers().remove(idx);
                layerListModel.remove(idx);
                layerJList.setSelectedIndex(0);
                drawingPanel.repaint();
            }
        });

        JButton btnLayerUp = new JButton("↑");
        btnLayerUp.addActionListener(e -> {
            int idx = layerJList.getSelectedIndex();
            if (idx > 0) {
                Collections.swap(drawingPanel.getLayers(), idx, idx - 1);
                Layer current = layerListModel.remove(idx);
                layerListModel.add(idx - 1, current);
                layerJList.setSelectedIndex(idx - 1);
                drawingPanel.repaint();
            }
        });

        JButton btnLayerDown = new JButton("↓");
        btnLayerDown.addActionListener(e -> {
            int idx = layerJList.getSelectedIndex();
            if (idx != -1 && idx < layerListModel.size() - 1) {
                Collections.swap(drawingPanel.getLayers(), idx, idx + 1);
                Layer current = layerListModel.remove(idx);
                layerListModel.add(idx + 1, current);
                layerJList.setSelectedIndex(idx + 1);
                drawingPanel.repaint();
            }
        });

        JButton btnToggleVisible = new JButton("Глаз");
        btnToggleVisible.addActionListener(e -> {
            Layer sel = layerJList.getSelectedValue();
            if (sel != null) {
                sel.setVisible(!sel.isVisible());
                layerJList.repaint(); 
                drawingPanel.repaint();
            }
        });

        JButton btnMoveToLayer = new JButton("Перенести выбранное в этот слой");
        btnMoveToLayer.addActionListener(e -> {
            Layer targetLayer = layerJList.getSelectedValue();
            if (targetLayer != null && !selectedShapes.isEmpty()) {
                List<AbstractShape> toMove = new ArrayList<>(selectedShapes);
                
                for (AbstractShape s : toMove) {
                    Layer sourceLayer = null;
                    for (Layer l : drawingPanel.getLayers()) {
                        if (l.getShapes().contains(s)) { sourceLayer = l; break; }
                    }
                    
                    if (sourceLayer != null && sourceLayer != targetLayer) {
                        commandHistory.execute(new MoveToLayerCommand(
                            Collections.singletonList(s), sourceLayer, targetLayer, drawingPanel));
                    }
                }
                clearSelection();
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Слои (Z-порядок)"));
        rightPanel.setPreferredSize(new Dimension(200, 0));

        JPanel layerControls = new JPanel(new GridLayout(2, 3));
        layerControls.add(btnAddLayer);
        layerControls.add(btnRemoveLayer);
        layerControls.add(btnToggleVisible);
        layerControls.add(btnLayerUp);
        layerControls.add(btnLayerDown);

        rightPanel.add(new JScrollPane(layerJList), BorderLayout.CENTER);
        rightPanel.add(layerControls, BorderLayout.NORTH);
        rightPanel.add(btnMoveToLayer, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        add(createToolBar(), BorderLayout.NORTH);
    }

    public Color getCurrentLineColor() {
        return this.currentLineColor;
    }
    public void setCurrentLineColor(Color color) {
        this.currentLineColor = color;
    }
    public Color getCurrentFillColor() {
        return this.currentFillColor;
    }
    public void setCurrentFillColor(Color color) {
        this.currentFillColor = color;
    }
    public float getCurrentThickness() {
        return this.currentThickness;
    }
    public void setCurrentThickness(float num) {
        this.currentThickness = num;
    }
    public ShapeType getCurrentShapeType() {
        return this.currentShapeType;
    }
    public void setCurrentShapeType(ShapeType type) {
        this.currentShapeType = type;
    }
    public DrawingPanel getDrawingPanel() {
        return this.drawingPanel;
    }
    public DrawingParameters getCurrentParameters() {
        return new DrawingParameters(getCurrentLineColor(), getCurrentFillColor(), getCurrentThickness());
    }
    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public ShapePlugin getActivePlugin() {
        return activePlugin;
    }

    public void setActivePlugin(ShapePlugin plugin) {
        this.activePlugin = plugin;
        if (plugin != null) {
            this.currentShapeType = null;
        }
    }

    public void copySelectedShapes() {
        if (selectedShapes.isEmpty()) {
            return;
        }

        Layer activeLayer = drawingPanel.getActiveLayer();
        List<AbstractShape> clones = new ArrayList<>();

        for (AbstractShape original : selectedShapes) {
            AbstractShape copy = original.cloneShape();
            
            copy.move(20, 20);
            
            copy.setSelected(false); 
            
            clones.add(copy);
        }

        Command copyCmd = new DrawShapeCommand(drawingPanel, activeLayer, clones);
        commandHistory.execute(copyCmd);

        clearSelection();
        for (AbstractShape s : clones) {
            addToSelection(s);
        }
        
        drawingPanel.repaint();
    }

    private void refreshLayerUI() {
        layerListModel.clear();
        
        List<Layer> updatedLayers = drawingPanel.getLayers();
        
        for (Layer layer : updatedLayers) {
            layerListModel.addElement(layer);
        }
        
        if (!layerListModel.isEmpty()) {
            layerJList.setSelectedIndex(0);
            drawingPanel.setActiveLayerIndex(0);
        }
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnCursor = new JButton("Select");
        btnCursor.addActionListener(e -> {
            setCurrentShapeType(ShapeType.CURSOR);
            setActivePlugin(null);
            clearSelection();
        });
        toolBar.add(btnCursor);
        toolBar.addSeparator();

        JButton btnLine = new JButton("Line");
        btnLine.addActionListener(e -> setCurrentShapeType(ShapeType.LINE));

        JButton btnRect = new JButton("Rectangle");
        btnRect.addActionListener(e -> setCurrentShapeType(ShapeType.RECTANGLE));

        JButton btnEllipse = new JButton("Ellipse");
        btnEllipse.addActionListener(e -> setCurrentShapeType(ShapeType.ELLIPSE));

        JButton btnPolyline = new JButton("Polyline");
        btnPolyline.addActionListener(e -> setCurrentShapeType(ShapeType.POLYLINE));

        JButton btnPolygon = new JButton("Polygon");
        btnPolygon.addActionListener(e -> setCurrentShapeType(ShapeType.POLYGON));

        toolBar.addSeparator();

        JButton btnLineColor = new JButton("Line Color");
        btnLineColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Line Color", currentLineColor);
            if (color != null) {
                currentLineColor = color;
                if (!selectedShapes.isEmpty()) {
                    commandHistory.execute(new ChangeParamsCommand(drawingPanel, new ArrayList<>(selectedShapes), getCurrentParameters()));
                }
            }
        });

        JButton btnFillColor = new JButton("Fill Color");
        btnFillColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Fill Color", currentFillColor);
            if (color != null) {
                currentFillColor = color;
                if (!selectedShapes.isEmpty()) {
                    commandHistory.execute(new ChangeParamsCommand(drawingPanel, new ArrayList<>(selectedShapes), getCurrentParameters()));
                }
            }
        });


        JButton btnNoFill = new JButton("No Fill");
        btnNoFill.addActionListener(e -> {
            currentFillColor = null;
            if (!selectedShapes.isEmpty()) {
                commandHistory.execute(new ChangeParamsCommand(drawingPanel, new ArrayList<>(selectedShapes), getCurrentParameters()));
            }
        });

        JSpinner spinnerThickness = new JSpinner(new SpinnerNumberModel(2.0, 1.0, 20.0, 1.0));
        spinnerThickness.addChangeListener(e -> {
            currentThickness = ((Double) spinnerThickness.getValue()).floatValue();
            if (!selectedShapes.isEmpty()) {
                commandHistory.execute(new ChangeParamsCommand(drawingPanel, new ArrayList<>(selectedShapes), getCurrentParameters()));
            }
        });

        JButton btnUndo = new JButton("Undo (Ctrl+Z)");
        btnUndo.addActionListener(e -> {
            commandHistory.undo();
            drawingPanel.repaint(); 
        });

        JButton btnRedo = new JButton("Redo (Ctrl+Y)");
        btnRedo.addActionListener(e -> {
            commandHistory.redo();
            drawingPanel.repaint();
        });

        JButton btnCopy = new JButton("Copy");
        btnCopy.addActionListener(e -> copySelectedShapes());

        JButton btnRotateLeft = new JButton("⟲");
        btnRotateLeft.addActionListener(e -> {
            if (!selectedShapes.isEmpty()) {
                commandHistory.execute(new RotateCommand(drawingPanel, new ArrayList<>(selectedShapes), -Math.toRadians(15)));
            }
        });

        JButton btnRotateRight = new JButton("⟳");
        btnRotateRight.addActionListener(e -> {
            if (!selectedShapes.isEmpty()) {
                commandHistory.execute(new RotateCommand(drawingPanel, new ArrayList<>(selectedShapes), Math.toRadians(15)));
            }
        });

        JButton btnScaleUp = new JButton("Size +");
        btnScaleUp.addActionListener(e -> {
            if (!getSelectedShapes().isEmpty()) {
                getCommandHistory().execute(new ScaleCommand(drawingPanel, getSelectedShapes(), 1.1));
            }
        });

        JButton btnScaleDown = new JButton("Size -");
        btnScaleDown.addActionListener(e -> {
            if (!getSelectedShapes().isEmpty()) {
                getCommandHistory().execute(new ScaleCommand(drawingPanel, getSelectedShapes(), 0.9));
            }
        });

        JButton btnSave = new JButton("Save JSON");
        btnSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    this.serializationManager.save(fileChooser.getSelectedFile().getAbsolutePath(), drawingPanel.getLayers());
                    JOptionPane.showMessageDialog(this, "Файл успешно сохранен!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Ошибка при сохранении: " + ex.getMessage());
                }
            }
        });

        JButton btnLoad = new JButton("Load JSON");
        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    List<Layer> loaded = this.serializationManager.load(fileChooser.getSelectedFile().getAbsolutePath());
                    
                    drawingPanel.getLayers().clear();
                    drawingPanel.getLayers().addAll(loaded);
                    
                    
                    refreshLayerUI(); 
                    
                    drawingPanel.repaint();
                    JOptionPane.showMessageDialog(this, "Файл успешно загружен!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Ошибка при загрузке: " + ex.getMessage());
                }
            }
        });

        toolBar.add(btnLine); toolBar.add(btnRect); toolBar.add(btnEllipse); toolBar.add(btnPolyline); toolBar.add(btnPolygon);
        
        toolBar.addSeparator();

        for (ShapePlugin plugin : loadedPlugins) {
            JButton btn = new JButton(plugin.getShapeName());
            btn.addActionListener(e -> {
                setCurrentShapeType(null);
                setActivePlugin(plugin);
            });
            toolBar.add(btn);
        }

        toolBar.addSeparator();
        
        toolBar.add(btnLineColor); toolBar.add(btnFillColor); toolBar.add(btnNoFill);
        
        toolBar.addSeparator();

        toolBar.add(btnScaleUp); toolBar.add(btnScaleDown);

        toolBar.addSeparator();
        
        toolBar.add(new JLabel(" Thick: ")); toolBar.add(spinnerThickness);
        
        toolBar.addSeparator();
        
        toolBar.add(btnRotateLeft); toolBar.add(btnRotateRight);
        
        toolBar.addSeparator();
        
        toolBar.add(btnUndo); toolBar.add(btnRedo); toolBar.add(btnCopy);

        toolBar.addSeparator();

        toolBar.add(btnSave); toolBar.add(btnLoad);

        return toolBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingApp app = new DrawingApp();
            app.setVisible(true);
        });
    }
}
