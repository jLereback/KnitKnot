package org.jlereback.knitknot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jlereback.knitknot.shapes.ShapeFactory;
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
		createGrid();
	}

	private void initPaintingArea() {
/*		context = paintingArea.getGraphicsContext2D();

        paintingArea.widthProperty().bindBidirectional(model.canvasWidthProperty());
        paintingArea.heightProperty().bindBidirectional(model.canvasHeightProperty());

        paintingArea.widthProperty().addListener(observable -> draw(context));
        paintingArea.heightProperty().addListener(observable -> draw(context));*/
	}

	private void initShape() {
		colorPicker.valueProperty().bindBidirectional(model.colorProperty());

		shapeType.valueProperty().bindBidirectional(model.shapeTypeProperty());
		shapeType.setItems(model.getChoiceBoxShapeList());

		sizeSpinner.getValueFactory().valueProperty().bindBidirectional(model.sizeProperty());

/*
		model.getCellList().addListener((ListChangeListener<FilledCell>) onChange -> draw(context));
*/
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

	void draw(GraphicsContext context) {
		preparePaintingArea();
		model.getCellList().forEach(cell -> cell.draw(context, model));
	}

	private void preparePaintingArea() {
/*
		context.setFill(BACKGROUND_COLOR);
		context.fillRect(0, 0, paintingArea.getWidth(), paintingArea.getHeight());
*/
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
	}

	public void resetClicked() {
		model.getRedoDeque().clear();
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
/*		if (model.isBrush()) {
			paintingArea.setOnMouseDragged(this::createNewShape);
			paintingArea.setOnMouseClicked(this::canvasClicked);
		} else
			paintingArea.setOnMouseDragged(this::shapeClicked);*/
	}

	private void createNewShape(MouseEvent mouseEvent) {

	}

	public void toggleEraser() {
/*		if (model.isEraser()) {
			paintingArea.setOnMouseDragged(this::erase);
			paintingArea.setOnMouseClicked(this::erase);
		} else {
			paintingArea.setOnMouseDragged(this::shapeClicked);
			paintingArea.setOnMouseClicked(this::canvasClicked);
		}*/
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

		popupController.setPopupStage(popupStage);

	}

	public void showPopup() {
		popupStage.show();
	}

	public void createGrid() {

		initGridCellCoordinates();

		setCanvasSize();

		ColumnConstraints column = new ColumnConstraints(model.getSize());
		grid.getColumnConstraints().clear();
		for (int i = 0; i < model.getColumn(); i++) {
			grid.getColumnConstraints().add(column);
		}
		RowConstraints row = new RowConstraints(model.getSize());
		grid.getRowConstraints().clear();
		for (int i = 0; i < model.getRow(); i++) {
			grid.getRowConstraints().add(row);
		}

		for (int i = 0; i < model.getColumn(); i++) {
			for (int j = 0; j < model.getRow(); j++) {
				AnchorPane pane = new AnchorPane();

				int finalI = i;
				int finalJ = j;
				double size = model.getSize();
				pane.setOnMouseClicked((event) -> {
							pane.getChildren().clear();
							if (finalI % 5 == 0 && finalJ % 5 == 0)
								pane.getChildren().add(Anims.fillCorner(model, size));
							else if (finalI % 5 == 0)
								pane.getChildren().add(Anims.fillColumnEdge(model, size));
							else if (finalJ % 5 == 0)
								pane.getChildren().add(Anims.fillRowEdge(model, size));

							else
								pane.getChildren().add(Anims.fillCell(model, size));
						}
				);

				pane.getStyleClass().add("game-grid-cell");
				if (i % 5 == 0) {
					pane.getStyleClass().add("first-column");
				}
				if (j % 5 == 0) {
					pane.getStyleClass().add("first-row");
				}
				grid.add(pane, i, j);
			}
		}
	}

	public static class Anims {
		static ShapeFactory shapeFactory = new ShapeFactory();

		//KLAR
		public static Node fillCell(Model model, double size) {
			var shape = shapeFactory.getShape(model.getShapeType(), size - 4, size - 4);
			//Rectangle req18 = new Rectangle(17, 17, model.getColor());
			shape.setFill(model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(1);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(1);
			group.getRowConstraints().add(row1);

			ColumnConstraints column2 = new ColumnConstraints(size - 4);
			group.getColumnConstraints().add(column2);
			group.add(shape, 1, 1);
			return group;
		}

		public static Node fillColumnEdge(Model model, double size) {
			var shape = shapeFactory.getShape(model.getShapeType(), size - 5, size - 4);
			//Rectangle req18 = new Rectangle(16, 17, model.getColor());
			shape.setFill(model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(2);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(1);
			group.getRowConstraints().add(row1);

			ColumnConstraints column2 = new ColumnConstraints(size - 3);
			group.getColumnConstraints().add(column2);
			group.add(shape, 1, 1);
			return group;
		}

		//KLAR
		public static Node fillRowEdge(Model model, double size) {
			var shape = shapeFactory.getShape(model.getShapeType(), size - 4, size - 5);
			//Rectangle req18 = new Rectangle(17, 16, model.getColor());
			shape.setFill(model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(1);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(2);
			group.getRowConstraints().add(row1);

			ColumnConstraints column2 = new ColumnConstraints(size - 3);
			group.getColumnConstraints().add(column2);
			group.add(shape, 1, 1);
			return group;
		}

		//KLAR
		public static Node fillCorner(Model model, double size) {
			var shape = shapeFactory.getShape(model.getShapeType(), size - 5, size - 5);
			shape.setFill(model.getColor());
			//Rectangle req18 = new Rectangle(16, 16, model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(2);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(2);
			group.getRowConstraints().add(row1);

			ColumnConstraints column2 = new ColumnConstraints(size - 3);
			group.getColumnConstraints().add(column2);
			group.add(shape, 1, 1);
			return group;
		}
	}


	private void setCanvasSize() {
		int canvasHeight = (int) (model.getRow() * model.getSize());
		int canvasWidth = (int) (model.getColumn() * model.getSize());

		model.setCanvasHeight(canvasHeight);
		model.setCanvasWidth(canvasWidth);
	}

	public void initGridCellCoordinates() {
		GridCellCoordinate[][] grid = new GridCellCoordinate[model.getRow().intValue()][model.getColumn().intValue()];

		double halfSize = model.getSize() / 2;

		for (int i = 0; i < model.getRow(); i++) {
			for (int j = 0; j < model.getColumn(); j++) {

				double x = (j * model.getSize()) + halfSize;
				double y = (i * model.getSize()) + halfSize;

				grid[i][j] = new GridCellCoordinate(x, y, model.getSize().intValue());
			}
		}

		model.setGrid(grid);
	}

}
