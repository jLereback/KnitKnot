package org.jlereback.knitknot;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jlereback.knitknot.shapes.ShapeFactory;
import org.jlereback.knitknot.shapes.shape.GridCellCoordinate;

public class PopupController {
	public Button createButton;
	public Button cancelButton;
	public Spinner<Double> rowSpinner;
	public Spinner<Double> columnSpinner;
	public Controller mainController;
	public Stage stage;
	public Model model;


	public void init() {
		rowSpinner.getValueFactory().valueProperty().bindBidirectional(model.rowProperty());
		columnSpinner.getValueFactory().valueProperty().bindBidirectional(model.columnProperty());
	}

	public void createClicked() {
		model.setRow(rowSpinner.getValue());
		model.setColumn(columnSpinner.getValue());
		createGrid();
		stage.close();
	}

	public void cancelClicked() {
		stage.close();
	}

	public void createGrid() {

		initGridCellCoordinates();

		setCanvasSize();

		ColumnConstraints column = new ColumnConstraints(model.getSize());
		mainController.grid.getColumnConstraints().clear();
		for (int i = 0; i < model.getColumn(); i++) {
			mainController.grid.getColumnConstraints().add(column);
		}
		RowConstraints row = new RowConstraints(model.getSize());
		mainController.grid.getRowConstraints().clear();
		for (int i = 0; i < model.getRow(); i++) {
			mainController.grid.getRowConstraints().add(row);
		}


		for (int i = 0; i < model.getColumn(); i++) {
			for (int j = 0; j < model.getRow(); j++) {
				AnchorPane pane = new AnchorPane();
				/*Canvas pane = new Canvas();
				GraphicsContext context = pane.getGraphicsContext2D();*/

				int finalI = i;
				int finalJ = j;
				pane.setOnMouseClicked((event) -> {
					if (finalI % 5 == 0 && finalJ % 5 == 0)
						pane.getChildren().add(Anims.fillRowCorner(mainController.model));
					else if (finalI % 5 == 0)
						pane.getChildren().add(Anims.fillColumnEdge(mainController.model));
					else if (finalJ % 5 == 0)
						pane.getChildren().add(Anims.fillRowEdge(mainController.model));

					else
						pane.getChildren().add(Anims.fillCell(mainController.model));
						}
				);

				pane.getStyleClass().add("game-grid-cell");
				if (i % 5 == 0) {
					pane.getStyleClass().add("first-column");
				}
				if (j % 5 == 0) {
					pane.getStyleClass().add("first-row");
				}
				mainController.grid.add(pane, i, j);
			}
		}
	}

	public static class Anims {
		static ShapeFactory shapeFactory;

		//KLAR
		public static Node fillCell(Model model) {
			Rectangle req18 = new Rectangle(17, 17, model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(1);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(1);
			group.getRowConstraints().add(row1);

			ColumnConstraints column18 = new ColumnConstraints(17);
			group.getColumnConstraints().add(column18);
			group.add(req18, 1, 1);
			return group;
		}

		public static Node fillColumnEdge(Model model) {
			Rectangle req18 = new Rectangle(16, 17, model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(2);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(1);
			group.getRowConstraints().add(row1);

			ColumnConstraints column18 = new ColumnConstraints(17);
			group.getColumnConstraints().add(column18);
			group.add(req18, 1, 1);
			return group;
		}

		//KLAR
		public static Node fillRowEdge(Model model) {
			Rectangle req18 = new Rectangle(17, 16, model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(1);
			group.getColumnConstraints().add(column1);

			RowConstraints row1 = new RowConstraints(2);
			group.getRowConstraints().add(row1);

			ColumnConstraints column18 = new ColumnConstraints(17);
			group.getColumnConstraints().add(column18);
			group.add(req18, 1, 1);
			return group;
		}

		//KLAR
		public static Node fillRowCorner(Model model) {
			Rectangle req1 = new Rectangle(2, 2, Color.TRANSPARENT);
			Rectangle req2 = new Rectangle(2, 2, Color.TRANSPARENT);
			Rectangle req18 = new Rectangle(16, 16, model.getColor());
			GridPane group = new GridPane();

			ColumnConstraints column1 = new ColumnConstraints(2);
			group.getColumnConstraints().add(column1);
			group.add(req1, 0, 1);

			RowConstraints row1 = new RowConstraints(2);
			group.getRowConstraints().add(row1);
			group.add(req2, 1, 0);

			ColumnConstraints column18 = new ColumnConstraints(17);
			group.getColumnConstraints().add(column18);
			group.add(req18, 1, 1);
			return group;		}
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

	public void setMainController(Controller controller) {
		this.mainController = controller;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setPopupStage(Stage stage) {
		this.stage = stage;
	}
}


