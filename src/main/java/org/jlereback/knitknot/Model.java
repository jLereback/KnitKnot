package org.jlereback.knitknot;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.jlereback.knitknot.shapes.ShapeType;
import org.jlereback.knitknot.shapes.shape.Shape;

import java.util.ArrayDeque;
import java.util.Deque;

public class Model {

    private final ObservableList<ShapeType> choiceBoxShapeList;
    private final ObjectProperty<ShapeType> shapeType;
    private final ObservableList<Shape> shapeList;
    private final Deque<Deque<Shape>> undoDeque;
    private final Deque<Deque<Shape>> redoDeque;
    private final ObjectProperty<Double> size;
    private final ObjectProperty<Double> row;
    private final ObjectProperty<Double> column;
    private final ObjectProperty<Color> color;
    private final BooleanProperty undoVisible;
    private final BooleanProperty redoVisible;
    private final DoubleProperty canvasHeight;
    private final DoubleProperty canvasWidth;
    private final BooleanProperty eraser;
    private final BooleanProperty brush;

    public Model() {
        this.row = new SimpleObjectProperty<>(80.0);
        this.column = new SimpleObjectProperty<>(10.0);
        this.canvasHeight = new SimpleDoubleProperty();
        this.canvasWidth = new SimpleDoubleProperty();
        this.undoVisible = new SimpleBooleanProperty(true);
        this.redoVisible = new SimpleBooleanProperty(true);
        this.eraser = new SimpleBooleanProperty(false);
        this.brush = new SimpleBooleanProperty(false);
        this.choiceBoxShapeList = FXCollections.observableArrayList(ShapeType.values());
        this.shapeList = FXCollections.observableArrayList();
        this.undoDeque = new ArrayDeque<>();
        this.redoDeque = new ArrayDeque<>();
        this.color = new SimpleObjectProperty<>(Color.web("#44966C"));
        this.size = new SimpleObjectProperty<>(20.0);
        this.shapeType = new SimpleObjectProperty<>(ShapeType.CIRCLE);
    }


    public Double getRow() {
        return row.get();
    }

    public ObjectProperty<Double> rowProperty() {
        return row;
    }

    public void setRow(Double row) {
        this.row.set(row);
    }

    public Double getColumn() {
        return column.get();
    }

    public ObjectProperty<Double> columnProperty() {
        return column;
    }

    public void setColumn(Double column) {
        this.column.set(column);
    }

    public double getCanvasWidth() {
        return canvasWidth.get();
    }

    public DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }
    public double getCanvasHeight() {
        return canvasHeight.get();
    }
    public DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public ObjectProperty<ShapeType> shapeTypeProperty() {
        return shapeType;
    }

    public ShapeType getShapeType() {
        return shapeType.get();
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType.set(shapeType);
    }

    public Property<Double> sizeProperty() {
        return size;
    }

    public Double getSize() {
        return size.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return color.get();
    }

    public BooleanProperty undoVisibleProperty() {
        return undoVisible;
    }

    public BooleanProperty redoVisibleProperty() {
        return redoVisible;
    }

    public boolean isEraser() {
        return eraser.get();
    }

    public BooleanProperty eraserProperty() {
        return eraser;
    }

    public boolean isBrush() {
        return brush.get();
    }

    public BooleanProperty brushProperty() {
        return brush;
    }

    public ObservableList<ShapeType> getChoiceBoxShapeList() {
        return choiceBoxShapeList;
    }

    public ObservableList<Shape> getShapeList() {
        return shapeList;
    }

    public Deque<Deque<Shape>> getUndoDeque() {
        return undoDeque;
    }

    public Deque<Deque<Shape>> getRedoDeque() {
        return redoDeque;
    }


    public void undo() {
        if (undoDeque.isEmpty())
            return;

        addToRedoDeque();
        shapeList.clear();
        shapeList.addAll(undoDeque.removeLast());
    }

    public void redo() {
        if (redoDeque.isEmpty())
            return;

        addToUndoDeque();
        shapeList.clear();
        shapeList.addAll(redoDeque.removeLast());
    }

    public Deque<Shape> getTempList() {
        Deque<Shape> tempList = new ArrayDeque<>();
        for (Shape shape : shapeList)
            tempList.add(shape.getShapeDuplicate());
        return tempList;
    }

    public void updateShapeList() {
        Deque<Shape> tempList = getTempList();
        shapeList.clear();
        shapeList.addAll(tempList);
    }

    public void addToUndoDeque() {
        undoDeque.addLast(getTempList());
    }

    public void addToRedoDeque() {
        redoDeque.addLast(getTempList());
    }

    public void addShapeToList(Shape shape) {
        shapeList.add(shape);
    }

    public void sendToList(Shape shape) {
        addShapeToList(shape);
    }
}