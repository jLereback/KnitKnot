package org.jlereback.knitknot;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

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
		mainController.createGrid();
		stage.close();
	}

	public void cancelClicked() {
		stage.close();
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


