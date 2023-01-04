package org.jlereback.knitknot;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class PopupController {
	public Button createButton;
	Model model = new Model();

	public void createGrid(ActionEvent actionEvent) {
		FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
		Controller controller = fxmlLoader.getController();
		controller.createGrid();
	}

	public void initialize() {
		createButton.cancelButtonProperty().bindBidirectional(model.createProperty());
	}

	public void setCreate(ActionEvent actionEvent) {
		model.setCreate(true);

	}

	//public void createGrid() {
	//	ColumnConstraints column = new ColumnConstraints(model.getSize());
	//	grid.getColumnConstraints().clear();
	//	for (int i = 0; i < model.getColumn(); i++) {
	//		grid.getColumnConstraints().add(column);
	//	}
	//	RowConstraints row = new RowConstraints(model.getSize());
	//	grid.getRowConstraints().clear();
	//	for (int i = 0; i < model.getRow(); i++) {
	//		grid.getRowConstraints().add(row);
	//	}
	//}
}
