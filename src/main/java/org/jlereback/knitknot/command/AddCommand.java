package org.jlereback.knitknot.command;

import org.jlereback.knitknot.Model;
import org.jlereback.knitknot.shapes.shape.FilledCell;

public class AddCommand implements Command {

	FilledCell cell;
	Model model;

	public AddCommand(FilledCell cell, Model model) {
		this.cell = cell;
		this.model = model;
	}

	@Override
	public void execute() {
		model.getCellList().add(cell);
	}

	@Override
	public void undo() {
		model.getCellList().remove(cell);
	}
}
