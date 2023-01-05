package org.jlereback.knitknot;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;

public class PopupController {
	public Button createButton;
	public Button cancelButton;
	public Spinner<Double> rowSpinner;
	public Spinner<Double> columnSpinner;
	public Controller mainController;
	public Stage stage;
	public Model model;


	public void initialize() {
		//rowSpinner.getValueFactory().valueProperty().bindBidirectional(model.rowProperty());
		//columnSpinner.getValueFactory().valueProperty().bindBidirectional(model.columnProperty());
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
