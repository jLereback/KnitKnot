package org.jlereback.knitknot;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.jlereback.knitknot.shapes.ShapeType;
import org.jlereback.knitknot.shapes.shape.FilledCell;
import org.jlereback.knitknot.shapes.shape.GridCellCoordinate;
import org.jlereback.knitknot.command.Command;

import java.util.ArrayDeque;
import java.util.Deque;

public class Model {

	private final ObservableList<ShapeType> choiceBoxShapeList;
	private final ObjectProperty<ShapeType> shapeType;
	private final ObservableList<FilledCell> cellList;
	private final Deque<Command> undoDeque;
	private final Deque<Command> redoDeque;
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
	private GridCellCoordinate[][] grid;

	public Model() {
		this.cellList = FXCollections.observableArrayList();
		this.row = new SimpleObjectProperty<>(20.0);
		this.column = new SimpleObjectProperty<>(28.0);
		this.canvasHeight = new SimpleDoubleProperty();
		this.canvasWidth = new SimpleDoubleProperty();
		this.undoVisible = new SimpleBooleanProperty(true);
		this.redoVisible = new SimpleBooleanProperty(true);
		this.eraser = new SimpleBooleanProperty(false);
		this.brush = new SimpleBooleanProperty(false);
		this.choiceBoxShapeList = FXCollections.observableArrayList(ShapeType.values());
		this.undoDeque = new ArrayDeque<>();
		this.redoDeque = new ArrayDeque<>();
		this.color = new SimpleObjectProperty<>(Color.web("#44966C"));
		this.size = new SimpleObjectProperty<>(20.0);
		this.shapeType = new SimpleObjectProperty<>(ShapeType.CIRCLE);
	}

	public ObservableList<FilledCell> getCellList() {
		return cellList;
	}

	public GridCellCoordinate[][] getGrid() {
		return grid;
	}

	public void setGrid(GridCellCoordinate[][] grid) {
		this.grid = grid;
	}

	public void setCanvasHeight(double canvasHeight) {
		this.canvasHeight.set(canvasHeight);
	}

	public void setCanvasWidth(double canvasWidth) {
		this.canvasWidth.set(canvasWidth);
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


	public Deque<Command> getUndoDeque() {
		return undoDeque;
	}

	public Deque<Command> getRedoDeque() {
		return redoDeque;
	}


	public void undo() {
		if (undoDeque.isEmpty())
			return;

		Command command = undoDeque.removeLast();
		command.undo();
		redoDeque.add(command);
	}

	public void redo() {
		if (redoDeque.isEmpty())
			return;
		Command command = redoDeque.removeLast();
		command.execute();
		undoDeque.add(command);
	}

	public Deque<FilledCell> getTempList() {
		Deque<FilledCell> tempList = new ArrayDeque<>();
		for (FilledCell cell : cellList)
			tempList.add(cell.getShapeDuplicate());
		return tempList;
	}

	public void updateShapeList() {
		Deque<FilledCell> tempList = getTempList();
		cellList.clear();
		cellList.addAll(tempList);
	}

	public void addToUndoDeque() {
	}
}