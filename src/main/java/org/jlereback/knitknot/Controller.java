package org.jlereback.knitknot;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jlereback.knitknot.shapes.ShapeFactory;
import org.jlereback.knitknot.shapes.ShapeParameter;
import org.jlereback.knitknot.shapes.ShapeType;
import org.jlereback.knitknot.shapes.shape.FilledCell;
import org.jlereback.knitknot.shapes.shape.GridCellCoordinate;
import org.jlereback.knitknot.shapes.shape.Shape;
import org.jlereback.knitknot.tools.SVGWriter;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;

public class Controller {
	static final KeyCombination SAVE = new KeyCodeCombination(KeyCode.S, CONTROL_DOWN);
	static final KeyCombination UNDO = new KeyCodeCombination(KeyCode.Z, CONTROL_DOWN);
	static final KeyCombination REDO = new KeyCodeCombination(KeyCode.Y, CONTROL_DOWN);
	static final KeyCombination EXIT = new KeyCodeCombination(KeyCode.E, ALT_DOWN);
	static final Color BACKGROUND_COLOR = Color.web("#edece0");

	public Model model = new Model();
	ShapeFactory shapeFactory = new ShapeFactory();
	private Stage stage;
	public GraphicsContext context;
	public ShapeParameter shapeParameter;
	public ChoiceBox<ShapeType> shapeType;
	public Spinner<Double> sizeSpinner;
	public ColorPicker colorPicker;
	public CheckMenuItem viewRedo;
	public CheckMenuItem viewUndo;
	public ToggleGroup equipment;
	public Canvas paintingArea;
	public ToggleButton eraser;
	public MenuItem newPattern;
	public ToggleButton brush;
	public Button undoButton;
	public Button redoButton;
	public MenuItem menuUndo;
	public MenuItem menuSave;
	public MenuItem menuRedo;
	public MenuItem menuExit;
	public GridPane grid;

	public void initialize() {

		initPaintingArea();
		initShape();
		initButtons();
		initMenu();


		preparePaintingArea();
	}

	private void initPaintingArea() {
		context = paintingArea.getGraphicsContext2D();

/*        paintingArea.widthProperty().bindBidirectional(model.canvasWidthProperty());
        paintingArea.heightProperty().bindBidirectional(model.canvasHeightProperty());

        paintingArea.widthProperty().addListener(observable -> draw());
        paintingArea.heightProperty().addListener(observable -> draw());*/
	}

	private void initShape() {
		colorPicker.valueProperty().bindBidirectional(model.colorProperty());

		shapeType.valueProperty().bindBidirectional(model.shapeTypeProperty());
		shapeType.setItems(model.getChoiceBoxShapeList());

		sizeSpinner.getValueFactory().valueProperty().bindBidirectional(model.sizeProperty());

		model.getCellList().addListener((ListChangeListener<FilledCell>) onChange -> draw());
	}

	private void initButtons() {
		viewUndo.selectedProperty().bindBidirectional(model.undoVisibleProperty());
		undoButton.visibleProperty().bind(model.undoVisibleProperty());

		viewRedo.selectedProperty().bindBidirectional(model.redoVisibleProperty());
		redoButton.visibleProperty().bind(model.redoVisibleProperty());

		brush.selectedProperty().bindBidirectional(model.brushProperty());
		eraser.selectedProperty().bindBidirectional(model.eraserProperty());
	}

	private void initMenu() {
		menuRedo.setAccelerator(REDO);
		menuUndo.setAccelerator(UNDO);
		menuSave.setAccelerator(SAVE);
		menuExit.setAccelerator(EXIT);
	}

	public void canvasClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isControlDown() || mouseEvent.isShiftDown())
			shapeClicked(mouseEvent);
		else {

			findCell(mouseEvent);

			//createNewShape(mouseEvent);
			//System.out.println(mouseEvent.getX());
			//System.out.println(mouseEvent.getY());

		}
		model.getRedoDeque().clear();
	}

	public void shapeClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isControlDown() && mouseEvent.isShiftDown())
			updateShape(mouseEvent);
		else if (mouseEvent.isControlDown())
			updateColor(mouseEvent);
		else if (mouseEvent.isShiftDown())
			updateSize(mouseEvent);
		else return;
		model.getRedoDeque().clear();
	}

	private void findCell(MouseEvent mouseEvent) {
		var grid = model.getGrid();
		for (int i = 0; i < model.getRow(); i++) {
			for (int j = 0; j < model.getColumn(); j++) {
				if (grid[i][j].isInsideCell(mouseEvent.getX(), mouseEvent.getY())) {

					grid[i][j].draw(context, model);
				}
			}
		}
	}

	private void createNewShape(MouseEvent mouseEvent) {
		createNewShapeParameter(mouseEvent.getX(), mouseEvent.getY());

		model.addToUndoDeque();
		model.sendToList(shapeFactory.getShape(model.getShapeType(), shapeParameter));
	}

	private void createNewShapeParameter(double posX, double posY) {
		shapeParameter = new ShapeParameter(posX, posY, model.getSize(), model.getColor());
	}

	private void draw() {
		preparePaintingArea();
		model.getCellList().forEach(cell -> cell.draw(context, model));
		System.out.println();
/*		model.getGridCellMap().stream()
				.filter(gridCellCoordinate -> gridCellCoordinate.getColor() != Color.TRANSPARENT)
				.forEach(cell -> cell.draw(context, model));*/
	}

	private void preparePaintingArea() {
		context.setFill(BACKGROUND_COLOR);
		context.fillRect(0, 0, paintingArea.getWidth(), paintingArea.getWidth());
	}

	public void undoClicked() {
		model.undo();
	}

	public void redoClicked() {
		model.redo();
	}

	private void erase(MouseEvent mouseEvent) {
		if (findShape(mouseEvent).isEmpty())
			return;
		model.addToUndoDeque();
		findShape(mouseEvent).ifPresent(shape -> model.getShapeList().remove(shape));
	}

	public void resetClicked() {
		model.getRedoDeque().clear();
		model.addToUndoDeque();
		preparePaintingArea();
		model.getShapeList().clear();
	}

	public void updateShape(MouseEvent mouseEvent) {
		if (findShape(mouseEvent).isEmpty())
			return;
		model.addToUndoDeque();
		findShape(mouseEvent).ifPresent(shape -> shape.updateShape(model.getColor(), model.getSize()));
		model.updateShapeList();
	}

	private void updateColor(MouseEvent mouseEvent) {
		if (findShape(mouseEvent).isEmpty())
			return;
		model.addToUndoDeque();
		findShape(mouseEvent).ifPresent(shape -> shape.setColor(model.getColor()));
		model.updateShapeList();
	}

	private void updateSize(MouseEvent mouseEvent) {
		if (findShape(mouseEvent).isEmpty())
			return;
		model.addToUndoDeque();
		findShape(mouseEvent).ifPresent(shape -> shape.setSize(model.getSize()));
		model.updateShapeList();
	}

	private Optional<Shape> findShape(MouseEvent mouseEvent) {
		return model.getShapeList().stream()
				.filter(shape -> shape.isInside(mouseEvent.getX(), mouseEvent.getY()))
				.reduce((first, second) -> second);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void save() {
		SVGWriter.getSVGWriter().save(model, stage);
	}

	public void exit() {
		System.exit(0);
	}

	public void toggleBrush() {
		if (model.isBrush()) {
			paintingArea.setOnMouseDragged(this::createNewShape);
			paintingArea.setOnMouseClicked(this::canvasClicked);
		} else
			paintingArea.setOnMouseDragged(this::shapeClicked);
	}

	public void toggleEraser() {
		if (model.isEraser()) {
			paintingArea.setOnMouseDragged(this::erase);
			paintingArea.setOnMouseClicked(this::erase);
		} else {
			paintingArea.setOnMouseDragged(this::shapeClicked);
			paintingArea.setOnMouseClicked(this::canvasClicked);
		}
	}


	public void loadPopup() throws IOException {
		//Load popup scene
		FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("popupView.fxml"));
		Parent root = popupLoader.load();

		//Get controller of popup
		PopupController popupController = popupLoader.getController();

		//Pass whatever data you want. You can have multiple method calls here
		popupController.setMainController(this);
		popupController.setModel(model);
		popupController.init();

		//Show popup in new window
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle("New Pattern");

		popupController.setPopupStage(stage);

		stage.show();

	}
}
