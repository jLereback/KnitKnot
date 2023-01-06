package org.jlereback.knitknot;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
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
import org.jlereback.knitknot.command.AddCommand;
import org.jlereback.knitknot.command.Command;
import org.jlereback.knitknot.command.EditCommand;
import org.jlereback.knitknot.tools.SVGWriter;

import java.io.IOException;
import java.util.Arrays;
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
	private static PopupController popupController;
	private static Stage popupStage;
	public GraphicsContext context;
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


		try {
			loadPopup();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		popupController.createGrid();
	}

	private void initPaintingArea() {
		context = paintingArea.getGraphicsContext2D();

        paintingArea.widthProperty().bindBidirectional(model.canvasWidthProperty());
        paintingArea.heightProperty().bindBidirectional(model.canvasHeightProperty());

        paintingArea.widthProperty().addListener(observable -> draw());
        paintingArea.heightProperty().addListener(observable -> draw());
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

			getCell(mouseEvent);

			//System.out.println(mouseEvent.getX());
			//System.out.println(mouseEvent.getY());

		}
		model.getRedoDeque().clear();
	}

	public void shapeClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isControlDown())
			updateColor(mouseEvent);
		else return;
		model.getRedoDeque().clear();
	}

	private void getCell(MouseEvent mouseEvent) {
		Arrays.stream(model.getGrid()).flatMap(Arrays::stream)
				.filter(point -> point.isInsideCell(mouseEvent.getX(), mouseEvent.getY()))
				.findFirst().ifPresent(this::setAddCommand);
	}

	private Optional<FilledCell> findCellInList(MouseEvent mouseEvent) {
		return model.getCellList().stream()
				.filter(filledCell -> filledCell.getCell().isInsideCell(mouseEvent.getX(), mouseEvent.getY()))
				.findFirst();
	}

	private void setAddCommand(GridCellCoordinate gridCell) {
		Command addCommand = new AddCommand(new FilledCell(gridCell, model.getColor()), model);
		addCommand.execute();
		model.getUndoDeque().add(addCommand);
	}

	private void draw() {
		preparePaintingArea();
		model.getCellList().forEach(cell -> cell.draw(context, model));
	}

	private void preparePaintingArea() {
		context.setFill(BACKGROUND_COLOR);
		context.fillRect(0, 0, paintingArea.getWidth(), paintingArea.getHeight());
	}

	public void undoClicked() {
		model.undo();
	}

	public void redoClicked() {
		model.redo();
	}

	private void erase(MouseEvent mouseEvent) {
		if (findCellInList(mouseEvent).isEmpty())
			return;
		model.addToUndoDeque();
	}

	public void resetClicked() {
		model.getRedoDeque().clear();
		model.addToUndoDeque();
		preparePaintingArea();
		model.getCellList().clear();
	}

	private void updateColor(MouseEvent mouseEvent) {
		if (findCellInList(mouseEvent).isEmpty())
			return;
		FilledCell oldCell = findCellInList(mouseEvent).get();

		FilledCell newCell = new FilledCell(oldCell.getCell(), model.getColor());

		Command command = new EditCommand(oldCell, newCell, model, model.getCellList().indexOf(oldCell));

		command.execute();
		model.getUndoDeque().add(command);
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

	private void createNewShape(MouseEvent mouseEvent) {

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
		popupController = popupLoader.getController();

		//Pass whatever data you want. You can have multiple method calls here
		popupController.setMainController(this);
		popupController.setModel(model);
		popupController.init();

		//Show popup in new window
		popupStage = new Stage();
		popupStage.setScene(new Scene(root));
		popupStage.setTitle("New Pattern");

		popupController.setPopupStage(stage);

	}


	public void showPopup() {
		popupStage.show();
	}


}
